package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.request.CustomerProfileDto;
import idiots.ddakdae.dto.response.LoginResponseDto;
import idiots.ddakdae.util.GCSUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerController {

    private final GCSUtil gcsUtil;

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "현재 로그인 사용자 API",
            description = "닉네임, 프로필이미지 반환, JWT 필수",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결과 메시지 반환",
                            content = @Content(schema = @Schema(implementation = CustomerProfileDto.class))
                    )
            })
    @GetMapping("/customer/me")
    public ResponseEntity<CustomerProfileDto> getCurrentCustomer(@AuthenticationPrincipal Customer customer) {
        if (Objects.isNull(customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String signedUrl = null;
        if (Objects.nonNull(customer.getProfileImage())) {
            signedUrl = gcsUtil.generateSignedUrl(customer.getProfileImage());
        }

        return ResponseEntity.ok(CustomerProfileDto.builder()
                .nickName(customer.getNickName())
                .profileImageUrl(signedUrl)
                .build());
    }
}
