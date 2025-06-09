package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.request.CustomerUpdateRequestDto;
import idiots.ddakdae.dto.request.SignUpRequestDto;
import idiots.ddakdae.dto.response.customer.CustomerDetailDto;
import idiots.ddakdae.dto.response.customer.CustomerProfileDto;
import idiots.ddakdae.exception.BizException;
import idiots.ddakdae.exception.ErrorCode;
import idiots.ddakdae.service.CustomerService;
import idiots.ddakdae.util.GCSUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerController {

    private final GCSUtil gcsUtil;
    private final CustomerService customerService;

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

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "현재 로그인 사용자의 상세정보 API",
            description = "다수 상세정보 반환 DTO 참조, JWT 필수",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결과 메시지 반환",
                            content = @Content(schema = @Schema(implementation = CustomerDetailDto.class))
                    )
            })
    @GetMapping("/customer/me/detail")
    public ResponseEntity<CustomerDetailDto> getCurrentCustomerDetail(@AuthenticationPrincipal Customer customer) {

        if (Objects.isNull(customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String imageUrl = null;
        if (Objects.nonNull(customer.getProfileImage())) {
            imageUrl = gcsUtil.generateSignedUrl(customer.getProfileImage());
        }

        CustomerDetailDto dto = CustomerDetailDto.builder()
                .email(customer.getEmail())
                .nickName(customer.getNickName())
                .phone(customer.getPhone())
                .carNumber(customer.getCarNumber())
                .carKnd(customer.getCarKnd())
                .manuCompany(customer.getManuCompany())
                .profileImageUrl(imageUrl)
                .carModel(customer.getCarModel())
                .fuelType(customer.getFuelType())
                .build();
        return ResponseEntity.ok(dto);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "현재 로그인 사용자의 상세정보 수정 API",
            description = "CustomerUpdateRequestDTO 참조해서 요청바람, JWT 필수",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결과 메시지 반환",
                            content = @Content(schema = @Schema(implementation = CustomerDetailDto.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "로그인 인증 실패"),
            })
    @PutMapping(value = "/customer/me/detail",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCustomerInfo(
            @AuthenticationPrincipal Customer customer,
            @Parameter(
                    description = "사용자 상세정보 수정요청 데이터(JSON)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomerUpdateRequestDto.class)
                    )
            )
            @RequestPart(value = "customerUpdateRequestDto") CustomerUpdateRequestDto customerUpdateRequestDto,
            @Parameter(schema = @Schema(type = "string", format = "binary"), description = "프로필 이미지 파일")
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

        if (Objects.isNull(customer)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        customerService.updateCustomer(customer.getId(), customerUpdateRequestDto, profileImage);
        return ResponseEntity.ok().build();
    }
}
