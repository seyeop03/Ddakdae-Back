package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.request.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class LoginController {

    @Operation(summary = "테스트용 로그인 API")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User user) {
        return "로그인 성공! 사용자 이메일: " + user.getAttribute("email");
    }

    @Operation(summary = "테스트용 로그인 API")
    @GetMapping("/protected")
    public String protectedResource(@AuthenticationPrincipal Customer customer) {
        log.info("customer: {}", customer);
        return "Hello, " + customer.getNickName();
    }
}

