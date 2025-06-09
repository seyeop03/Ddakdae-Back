package idiots.ddakdae.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    /** 리뷰 내용 **/
    private String comment;

    /** 별점 **/
    private Integer star;

    /** 이미지 **/
    private String reviewImagePath;

    /** 이미지 참고 내용 **/
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pl_id")
    private ParkingLot parkingLot;

    private LocalDateTime createdAt;
}
