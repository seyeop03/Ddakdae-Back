package idiots.ddakdae.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RealTimeParkingDto {
    @JsonProperty("PKLT_CD")
    private String pkltCd;

    @JsonProperty("TPKCT")
    private String tpkct;

    @JsonProperty("NOW_PRK_VHCL_CNT")
    private String nowPrkVhclCnt;

    @JsonProperty("NOW_PRK_VHCL_UPDT_TM")
    private String nowPrkVhclCntUpdtTm; // 파싱 후 Timestamp로 변환
}
