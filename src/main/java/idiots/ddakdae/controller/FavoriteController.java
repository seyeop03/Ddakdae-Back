package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.response.clustering.NearbyParkingDto;
import idiots.ddakdae.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    @Operation(
            summary = "로그인 사용자 찜 등록 API",
            description = "특정 사용자가 찜 등록, JWT & parkingLotId 필수",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결과 메시지 반환",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            })
    public ResponseEntity<?> addFavorite(
            @AuthenticationPrincipal Customer customer,
            @RequestParam Long parkingLotId
    ) {
        if (Objects.isNull(customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        favoriteService.addFavorite(customer.getId(), parkingLotId);
        return ResponseEntity.ok("찜 추가 완료");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping
    @Operation(
            summary = "로그인 사용자 찜 삭제 API",
            description = "특정 사용자가 찜 삭제, JWT & parkingLotId 필수",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결과 메시지 반환",
                            content = @Content(schema = @Schema(implementation = String.class))
                    )
            })
    public ResponseEntity<?> removeFavorite(
            @AuthenticationPrincipal Customer customer,
            @RequestParam Long parkingLotId
    ) {
        if (Objects.isNull(customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        favoriteService.removeFavorite(customer.getId(), parkingLotId);
        return ResponseEntity.ok("찜 삭제 완료");
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "로그인 사용자 찜 목록 조회 API",
            description = "특정 사용자에 대한 찜 목록, JWT & parkingLotId 필수, 주차장 DTO 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주차장 목록 결과",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NearbyParkingDto.class)))
                    )
            })
    @GetMapping
    public ResponseEntity<?> getFavorites(
            @AuthenticationPrincipal Customer customer
    ) {
        if (Objects.isNull(customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        return ResponseEntity.ok(favoriteService.getFavorites(customer.getId()));
    }
}
