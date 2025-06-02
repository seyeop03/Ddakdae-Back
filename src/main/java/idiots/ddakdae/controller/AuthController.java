package idiots.ddakdae.controller;

import idiots.ddakdae.dto.request.LoginRequestDto;
import idiots.ddakdae.dto.response.LoginResponseDto;
import idiots.ddakdae.login.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "로컬 로그인 API",
            description = "이메일 패스워드 필수, 현재 회원가입 기능이 안 되어있으므로 요청파라미터 하드코딩 필요",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "결과 메시지 반환",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class))
                    )
            })
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
}
