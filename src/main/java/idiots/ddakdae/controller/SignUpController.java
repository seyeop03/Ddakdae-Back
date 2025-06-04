package idiots.ddakdae.controller;

import idiots.ddakdae.dto.request.SignUpRequestDto;
import idiots.ddakdae.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SignUpController {

    private final CustomerService customerService;

    @Operation(summary = "회원가입 API", description = "회원가입 폼 및 프로필 이미지는 하나만 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류")
    })
    @PostMapping(value = "/signup",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signup(
            @Parameter(
                    description = "회원가입 폼 데이터(JSON)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SignUpRequestDto.class)
                    )
            )
            @RequestPart("signupRequestDto") @Valid SignUpRequestDto signUpRequestDto,
                                    @Parameter(schema = @Schema(type = "string", format = "binary"), description = "프로필 이미지 파일")
                                    @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        customerService.signup(signUpRequestDto, profileImage);
        return ResponseEntity.ok().build();
    }
}
