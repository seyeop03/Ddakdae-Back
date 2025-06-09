package idiots.ddakdae.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Schema(defaultValue = "페이지 응답 DTO")
public class PageResponse<T> {

    @Schema(description = "현재 페이지 데이터 목록")
    private List<T> content;

    @Schema(description = "총 아이템 수")
    private long totalElements;

    @Schema(description = "총 페이지 수")
    private int totalPages;

    @Schema(description = "현재 페이지 (0부터 시작)")
    private int page;

    @Schema(description = "페이지당 아이템 수")
    private int size;

    @Schema(description = "마지막 페이지 여부")
    private boolean last;

    @Schema(description = "첫 페이지 여부")
    private boolean first;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.last = page.isLast();
        this.first = page.isFirst();
    }

}
