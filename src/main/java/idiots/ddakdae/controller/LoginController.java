package idiots.ddakdae.controller;

import idiots.ddakdae.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class LoginController {

    @GetMapping("/login-success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User user) {
        return "로그인 성공! 사용자 이메일: " + user.getAttribute("email");
    }

    @GetMapping("/protected")
    public String protectedResource(@AuthenticationPrincipal Customer customer) {
        log.info("customer: {}", customer);
        return "Hello, " + customer.getNickName();
    }

}

