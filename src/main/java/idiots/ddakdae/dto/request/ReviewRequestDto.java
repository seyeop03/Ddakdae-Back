package idiots.ddakdae.dto.request;

import lombok.Data;

@Data
public class ReviewRequestDto {

    private String comment;
    private Integer star;
    private String desc;
    private Long plId;
}
