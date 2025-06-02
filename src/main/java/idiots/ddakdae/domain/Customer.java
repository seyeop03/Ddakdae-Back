package idiots.ddakdae.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long id;

    /** 로컬 이메일 */
    private String email;

    /** 로컬 패스워드 */
    private String password;

    /** 닉네임 */
    private String nickName;

    /** 차 번호 */
    @Column(name = "cust_car_number")
    private String carNumber;

    /** 차 종류 */
    @Column(name = "cust_car_knd")
    private String carKnd;

    /** SNS ID */
    private String snsId;

    /** 현재 SNS 로그인 명 */
    private String snsType;

    /** SNS Email */
    private String snsEmail;

    /** 연결시킨 SNS 명 */
    private String connectedSns;

    /** 폰 넘버 */
    private String phone;

    /** 프로필 이미지 */
    @Column(name = "profile_image_path")
    private String profileImage;

    /** 차 회사명 */
    private String manuCompany;

    /** 엔진 타입 */
    private String fuelType;

    /** 차 모델명 */
    private String carModel;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
