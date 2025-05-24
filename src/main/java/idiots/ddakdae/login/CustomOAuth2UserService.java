package idiots.ddakdae.login;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final CustomerRepository customerRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(req);
        String provider = req.getClientRegistration().getRegistrationId(); // kakao, naver, google

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String snsId = extractSnsId(provider, attributes);
        String snsEmail = extractEmail(provider, attributes);
        String name = extractName(provider, attributes);

        log.info("snsEmail: {}, name: {}", snsEmail, name);

        Customer customer = customerRepository.findBySnsTypeAndSnsId(provider, snsId)
                .orElseGet(() -> {
                    Customer newUser = new Customer();
                    newUser.setSnsType(provider);
                    newUser.setSnsId(snsId);
                    newUser.setSnsEmail(snsEmail);
                    newUser.setNickName(name);
                    return customerRepository.save(newUser);
                });

        // attributes에 email 추가 (Spring Security에서 nameAttributeKey로 사용하기 위함)
        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("email", snsEmail); // nameKey로 사용
        customAttributes.put("name", name);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "email",
                customer
        );
    }

    private String extractSnsId(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                return String.valueOf(attributes.get("id"));
            case "naver":
                return String.valueOf(((Map<String, Object>) attributes.get("response")).get("id"));
            case "google":
                return String.valueOf(attributes.get("sub")); // 구글은 'sub'가 고유 ID
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }


    private String extractEmail(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
            case "naver":
                return (String) ((Map<String, Object>) attributes.get("response")).get("email");
            case "google":
                return (String) attributes.get("email");
            default:
                return null;
        }
    }

    private String extractName(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                return (String) profile.get("nickname");
            case "naver":
                return (String) ((Map<String, Object>) attributes.get("response")).get("name");
            case "google":
                return (String) attributes.get("name");
            default:
                return "unknown";
        }
    }
}
