package idiots.ddakdae.login.filter;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.login.JwtProvider;
import idiots.ddakdae.repository.CustomerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomerRepository customerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (Objects.nonNull(token)) {
            try {
                log.info("requested JWT: {}", token);
                Long id = jwtProvider.getUserIdFromToken(token);
                log.info("parsing completed custId: {}", id);
                Customer customer = customerRepository.findById(id).orElse(null);

                if (customer == null) {
                    log.warn("Customer not found for id: {}", id);
                    // 인증 실패 → 필터만 넘김
                    filterChain.doFilter(request, response);
                    return;
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        customer, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                log.info("SecurityContext principal class: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass());

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication success: {}", customer.getNickName());
            } catch (Exception e) {
                log.warn("JWT Authentication Failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        log.info("auth: {}", auth);
        if (Objects.nonNull(auth) && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
    }
}
