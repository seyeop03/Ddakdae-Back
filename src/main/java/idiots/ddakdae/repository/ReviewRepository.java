package idiots.ddakdae.repository;

import idiots.ddakdae.domain.Review;
import idiots.ddakdae.dto.response.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT new idiots.ddakdae.dto.response.ReviewResponseDto(
            r.reviewId, r.comment, r.star, r.description, r.reviewImagePath,
            c.nickName, c.profileImage, r.createdAt
        )
        FROM Review r
        INNER JOIN r.customer c
        INNER JOIN r.parkingLot p
        WHERE p.plId = :plId
        ORDER BY r.createdAt DESC
    """)
    Page<ReviewResponseDto> findAllByParkingLotPlId(Long plId, Pageable pageable);
}

