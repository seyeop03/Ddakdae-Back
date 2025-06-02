package idiots.ddakdae.login;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.request.LoginRequestDto;
import idiots.ddakdae.dto.response.LoginResponseDto;
import idiots.ddakdae.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Customer customer = customerRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (Objects.nonNull(customer.getSnsType())) {
            throw new IllegalArgumentException("소셜 로그인 전용 계정입니다.");
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtProvider.createToken(customer.getId());

        return new LoginResponseDto(token);
    }
}
