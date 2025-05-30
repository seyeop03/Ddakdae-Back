package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Coordinates;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.dto.request.AddressDto;
import idiots.ddakdae.dto.request.MapBoundsRequest;
import idiots.ddakdae.dto.response.ResponseDto;
import idiots.ddakdae.dto.response.clustering.DongClusterDto;
import idiots.ddakdae.dto.response.clustering.GuClusterDto;
import idiots.ddakdae.dto.response.clustering.MarkerDto;
import idiots.ddakdae.service.GeocodeService;
import idiots.ddakdae.service.KakaoMapService;
import idiots.ddakdae.service.NaverMapService;
import idiots.ddakdae.service.ParkingLotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GeocodeController {

    private final GeocodeService geocodeService;
    private final ParkingLotService parkingLotService;
    private final KakaoMapService kakaoMapService;
    private final NaverMapService naverMapService;

    @Operation(
            summary = "첫 메인화면 지도 클러스터링",
            description = "줌레벨에 따라 구/동/마커 DTO 중 하나를 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "클러스터링 결과",
                            content = @Content(array = @ArraySchema(schema = @Schema(oneOf = {
                                    GuClusterDto.class,
                                    DongClusterDto.class,
                                    MarkerDto.class
                            })))
                    )
            }
    )
    @PostMapping("/pklt")
    public ResponseEntity<?> getClusteredPkltData(@RequestBody MapBoundsRequest mapBoundsRequest) {
        List<?> clusteredData = parkingLotService.getClusteredData(mapBoundsRequest);
        return ResponseEntity.ok(clusteredData);
    }

    @PostMapping("/geocode")
    public ResponseEntity<?> geocode(@RequestBody AddressDto addressDto,
                                     @RequestParam(defaultValue = "1000") int radius) throws Exception {
        // 주소를 위도와 경도로 변환
        Coordinates coordinates = geocodeService.getCoordinatesFromAddress(addressDto.getAddress());

        // 반경 내 주차장 검색 (미터 단위)
        List<ParkingLot> parkingLots = parkingLotService.getParkingLotsWithinRadius(
                coordinates.getLongitude(),
                coordinates.getLatitude(),
                radius
        );
        log.info("Found {} parking lots within {} meters", parkingLots.size(), radius);

        // 클라이언트에게 주차장 정보 반환
        return ResponseEntity.ok().body(ResponseDto.success(parkingLots));
    }


    @GetMapping("/geocode")
    public String getLatLng(@RequestParam String address) {
        return naverMapService.getCoordinates(address);
    }

    @Operation(summary = "키워드 검색 API", description = "기본값 1페이지당 5개씩, 랜덤 정렬")
    @GetMapping("/search/local")
    public ResponseEntity<Object> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int display,
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "random") String sort) {
        Object result = naverMapService.searchLocal(query, display, start, sort);
        return ResponseEntity.ok(result);
    }
}
