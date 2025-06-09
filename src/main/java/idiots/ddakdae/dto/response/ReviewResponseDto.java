package idiots.ddakdae.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "리뷰 상세정보 DTO")
public class ReviewResponseDto {

    @Schema(description = "리뷰 코멘트")
    private String comment;

    @Schema(description = "리뷰 별점")
    private int star;

    @Schema(description = "이미지 설명")
    private String description;

    @Schema(description = "리뷰 이미지경로")
    private String reviewImagePath;

    @Schema(description = "작성자 닉네임")
    private String nickName;

    @Schema(description = "작성자 프로필 이미지경로")
    private String profileImagePath;
}
