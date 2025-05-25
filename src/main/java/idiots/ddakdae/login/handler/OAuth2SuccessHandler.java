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

//        response.sendRedirect("http://localhost:5173/login/callback?token=" + token);
        response.sendRedirect("http://ddak-dae.kro.kr/login/callback?token=" + token);
    }
}
