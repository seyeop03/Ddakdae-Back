package idiots.ddakdae.dto.response.clustering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "거리 마커 단위 클러스터 DTO")
public class MarkerDto {

    @Schema(description = "주자창 명")
    private String pkltNm;

    @Schema(description = "주자창 종류명")
    private String pkltKndNm;

    @Schema(description = "주차 기본 요금 (1시간 단위)")
    private Integer hourlyPrice;

    @Schema(description = "추가 요금 (10분 단위)")
    private Integer addCrg10Mnt;

    @Schema(description = "위도")
    private BigDecimal lat;

    @Schema(description = "경도")
    private BigDecimal lot;
}
