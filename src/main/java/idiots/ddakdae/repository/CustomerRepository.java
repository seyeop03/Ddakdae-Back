package idiots.ddakdae.repository;

import idiots.ddakdae.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email); // 로컬용

    Optional<Customer> findBySnsTypeAndSnsId(String snsType, String snsId); // SNS용
}

