package idiots.ddakdae.dto.response;

import idiots.ddakdae.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Schema(description = "현재 주차 차량 수 업데이트 DTO")
public class RealTimeParkingResponseDto {

    @Schema(description = "주차장 ID")
    private Long plId;

    @Schema(description = "주차장 명")
    private String pklt_nm;

    @Schema(description = "총 주차 면적 수")
    private Integer tpkct;

    @Schema(description = "현재 주차 차량 수")
    private Integer nowPrkVhclCnt;

    @Schema(description = "API 호출 후 마지막 업데이트 시간")
    private Timestamp nowPrkVhclCntUpdtTm;

    public String getNowPrkVhclCntUpdtTm() {
        if (nowPrkVhclCntUpdtTm == null) return null;
        return TimeUtil.convertUtcIsoToKst(nowPrkVhclCntUpdtTm.toInstant().toString());
    }
}
