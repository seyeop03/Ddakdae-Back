package idiots.ddakdae.dto.response.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "로그인 후 현재 아이디 기반 DTO")
public class CustomerProfileDto {

    @Schema(description = "현재 사용자 닉네임")
    private String nickName;

    @Schema(description = "현재 사용자 프로필 이미지")
    private String profileImageUrl;
}
