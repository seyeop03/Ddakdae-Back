package idiots.ddakdae.login;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.dto.request.LoginRequestDto;
import idiots.ddakdae.dto.response.LoginResponseDto;
import idiots.ddakdae.exception.BizException;
import idiots.ddakdae.exception.ErrorCode;
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
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));

        if (Objects.nonNull(customer.getSnsType())) {
            throw new BizException(ErrorCode.EXIST_SNS_EMAIL);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), customer.getPassword())) {
            throw new BizException(ErrorCode.INCORRECT_PASSWORD);
        }

        String token = jwtProvider.createToken(customer.getId());

        return new LoginResponseDto(token);
    }
}
