package idiots.ddakdae.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.dto.request.MapBoundsRequest;
import idiots.ddakdae.dto.request.NearbyParkingRequest;
import idiots.ddakdae.dto.response.ParkingLotResponse;
import idiots.ddakdae.dto.response.clustering.NearbyParkingDetailDto;
import idiots.ddakdae.dto.response.clustering.NearbyParkingDto;
import idiots.ddakdae.infra.redis.RedisCacheKeyGenerator;
import idiots.ddakdae.repository.ParkingLotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public void importJsonData(String jsonFilePath) throws IOException {
        File file = new File(jsonFilePath);

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);


        // JSON 배열로 저장된 파일을 리스트로 변환
        ParkingLotResponse response = objectMapper.readValue(file, ParkingLotResponse.class);
        List<ParkingLot> lots = response.getData().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        parkingLotRepository.saveAll(lots);
    }

    public List<?> getClusteredData(MapBoundsRequest req) {
        String key = RedisCacheKeyGenerator.generateKey(
                req.getSwLat(), req.getNeLat(), req.getSwLot(), req.getNeLot(), req.getZoomLevel());

        List<?> cached = (List<?>) redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(cached)) {
            return cached;
        }

        List<?> result;
        int zoomLevel = req.getZoomLevel();
        if (zoomLevel <= 12) {
            result = parkingLotRepository.groupByGu(req.getSwLat(), req.getNeLat(), req.getSwLot(), req.getNeLot());
        } else if (zoomLevel <= 14) {
            result = parkingLotRepository.groupByDong(req.getSwLat(), req.getNeLat(), req.getSwLot(), req.getNeLot());
        } else {
            result = parkingLotRepository.getMarkers(req.getSwLat(), req.getNeLat(), req.getSwLot(), req.getNeLot());
        }

        redisTemplate.opsForValue().set(key, result, Duration.ofSeconds(30));
        return result;
    }

    public List<ParkingLot> getParkingLotsWithinRadius(double latitude, double longitude, int radiusMeters) {
        double radiusKm = radiusMeters / 1000.0; // 미터를 킬로미터로 변환
        List<ParkingLot> parkingLots = parkingLotRepository.findParkingLotsWithinRadius(latitude, longitude, radiusKm);
        log.info("Service: Found {}, parking lots for lat= {}, lon= {}, radius= {}km",
                parkingLots.size(), latitude, longitude, radiusKm);
        return parkingLots;
    }

    public Page<NearbyParkingDto> getNearbyParkingLots(NearbyParkingRequest req) {
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());
        return parkingLotRepository.findNearbyParkingLots(
            req.getLat(), req.getLot(), req.getRadius(), pageable
        );
    }

    public NearbyParkingDetailDto getParkingLotDetail(Long id) {
        return parkingLotRepository.findParkingLotDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("주차장 없음"));
    }
}