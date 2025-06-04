package idiots.ddakdae.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "회원가입 DTO")
public class SignUpRequestDto {

    @NotBlank
    @Size(min = 2, max = 8)
    @Pattern(regexp = "^[a-zA-z0-9가-힣]+$")
    @Schema(description = "닉네임 한글이나 소문자 포함")
    private String nickName;

    @NotBlank
    @Email
    @Size(min = 8, max = 50)
    @Schema(description = "최소 8글자 ~ 최대 50글자")
    private String email;

    @NotBlank
    @Size(min = 8)
    @Schema(description = "최소 8글자")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,11}$")
    @Schema(description = "숫자만 입력")
    private String phone;

    // 선택
    @Schema(description = "고객 차량 번호 (선택)")
    private String custCarNumber;

    @Schema(description = "고객 차량 종류 (선택)")
    private String custCarKind;

    @Schema(description = "고객 차량 제조사 (선택)")
    private String manuCompany;

    @Schema(description = "고객 차량 연료타입 (선택)")
    private String fuelType;

    @Schema(description = "고객 차량 모델 (선택)")
    private String carModel;
}
