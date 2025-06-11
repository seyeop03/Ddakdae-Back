package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Coordinates;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.dto.request.AddressDto;
import idiots.ddakdae.dto.request.MapBoundsRequest;
import idiots.ddakdae.dto.request.NearbyParkingRequest;
import idiots.ddakdae.dto.response.RealTimeParkingResponseDto;
import idiots.ddakdae.dto.response.ResponseDto;
import idiots.ddakdae.dto.response.clustering.*;
import idiots.ddakdae.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final PkltRealTimeService pkltRealTimeService;

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

    @Operation(
            summary = "주차장 목록",
            description = "주차장 목록 API, 클릭 시 상세정보 API로 연결됨",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주차장 목록 결과",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NearbyParkingDto.class)))
                    )
            }
    )
    @PostMapping("/nearby")
    public ResponseEntity<?> getNearbyParkingLots(@RequestBody NearbyParkingRequest req) {
        Page<NearbyParkingDto> nearbyParkingLots = parkingLotService.getNearbyParkingLots(req);
        return ResponseEntity.ok(nearbyParkingLots);
    }

    @Operation(
            summary = "주차장 상세 목록",
            description = "주차장 상세 목록 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주차장 상세 목록 결과",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NearbyParkingDetailDto.class)))
                    )
            }
    )
    @GetMapping("/nearby/{plId}")
    public ResponseEntity<NearbyParkingDetailDto> getDetail(@PathVariable Long plId) {
        return ResponseEntity.ok(parkingLotService.getParkingLotDetail(plId));
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


    @Operation(
            summary = "5분 주기 현재 주차 차량 수 업데이트 API",
            description = "연계 데이터 약 100건으로 작음, 프론트에서 setInterval 처리 가능할 듯",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "현재 주차 차량 수 업데이트 주차장 목록 결과",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RealTimeParkingResponseDto.class)))
                    )
            })
    @GetMapping("pklt/realtime")
    public List<?> getAllRealTimePkltData() {
        return pkltRealTimeService.getAll();
    }
}
