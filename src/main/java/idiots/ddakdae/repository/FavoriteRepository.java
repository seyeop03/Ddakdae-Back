package idiots.ddakdae.repository;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.domain.Favorite;
import idiots.ddakdae.domain.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByCustomerAndParkingLot(Customer customer, ParkingLot parkingLot);

    Optional<Favorite> findByCustomerAndParkingLot(Customer customer, ParkingLot parkingLot);

    List<Favorite> findAllByCustomer(Customer customer);
}
