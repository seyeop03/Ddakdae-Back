package idiots.ddakdae.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.dto.response.ParkingLotResponse;
import idiots.ddakdae.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ObjectMapper objectMapper;

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

    public List<ParkingLot> getParkingLotsWithinRadius(double latitude, double longitude, int radiusMeters) {
        double radiusKm = radiusMeters / 1000.0; // 미터를 킬로미터로 변환
        List<ParkingLot> parkingLots = parkingLotRepository.findParkingLotsWithinRadius(latitude, longitude, radiusKm);
        System.out.println("Service: Found " + parkingLots.size() + " parking lots for lat=" + latitude +
                ", lon=" + longitude + ", radius=" + radiusKm + "km");
        return parkingLots;
    }
}