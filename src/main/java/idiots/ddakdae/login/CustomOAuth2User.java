package idiots.ddakdae.login;

import idiots.ddakdae.domain.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private final Customer customer;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, Customer customer) {
        super(authorities, attributes, nameAttributeKey);
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
