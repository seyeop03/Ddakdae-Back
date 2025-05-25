package idiots.ddakdae.dto.response.clustering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "구 단위 클러스터 DTO")
public class GuClusterDto {

    @Schema(description = "자치구 명")
    private String gu;

    @Schema(description = "해당 구 전체 주차장 개수")
    private long count;

    @Schema(description = "평균 위치 위도")
    private double avgLat;

    @Schema(description = "평균 위치 경도")
    private double avgLot;
}
