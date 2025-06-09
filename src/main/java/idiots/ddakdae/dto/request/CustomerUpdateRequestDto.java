package idiots.ddakdae.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdateRequestDto {

    private String nickName;

    private String phone;

    private String carNumber;

    private String carKnd;

    private String manuCompany;

    private String fuelType;

    private String carModel;
}
