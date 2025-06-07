package idiots.ddakdae.dto.response.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "로그인 사용자 상세정보 DTO")
public class CustomerDetailDto {

    private String profileImageUrl;

    private String nickName;

    private String email;

    private String phone;

    private String carNumber;

    private String carKnd;

    private String manuCompany;

    private String fuelType;

    private String carModel;
}
