package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Coordinates;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.dto.request.AddressDto;
import idiots.ddakdae.dto.response.ResponseDto;
import idiots.ddakdae.service.GeocodeService;
import idiots.ddakdae.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GeocodeController {

    private final GeocodeService geocodeService;
    private final ParkingLotService parkingLotService;


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
        System.out.println("Found " + parkingLots.size() + " parking lots within " + radius + " meters");

        // 클라이언트에게 주차장 정보 반환
        return ResponseEntity.ok().body(ResponseDto.success(parkingLots));
    }
}
