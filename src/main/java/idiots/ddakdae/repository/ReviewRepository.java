package idiots.ddakdae.repository;

import idiots.ddakdae.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByParkingLotPlId(Long plId);
}

