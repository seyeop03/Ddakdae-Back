package idiots.ddakdae.login.handler;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.login.CustomOAuth2User;
import idiots.ddakdae.login.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
        Customer customer = customUser.getCustomer();

        String token = jwtProvider.createToken(customer.getId());

        String referer = request.getHeader("Referer"); // or Origin
        String redirectBase;

        if (referer != null && referer.contains("ddak-dae.kro.kr")) {
            redirectBase = "http://ddak-dae.kro.kr";
        } else {
            redirectBase = "http://localhost:5173";
        }

        response.sendRedirect(redirectBase + "/login/callback?token=" + token);
    }
}
