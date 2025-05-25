package idiots.ddakdae.dto.response.clustering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "거리 마커에서 주차장 목록 DTO, 범위 조절 가능")
public class NearbyParkingDto {

    @Schema(description = "주차장 식별 ID")
    private Long id;

    @Schema(description = "주차장 명")
    private String pkltNm;

    @Schema(description = "주차장 주소 (지번)")
    private String addr;

    @Schema(description = "주차장 종류명")
    private String pkltKndNm;

    @Schema(description = "주차장 총 주차면 수")
    private Integer tpkct;

    @Schema(description = "유무료 구분명")
    private String chgdFreeNm;

    @Schema(description = "야간무료개방 여부명")
    private String nghtFreeOpnYnName;

    @Schema(description = "평일 운영 시작시각(HHMM)")
    private String wdOperBgngTm;

    @Schema(description = "평일 운영 종료시각(HHMM)")
    private String wdOperEndTm;

    @Schema(description = "주말 운영 시작시각(HHMM)")
    private String weOperBgngTm;

    @Schema(description = "주말 운영 종료시각(HHMM)")
    private String weOperEndTm;

    @Schema(description = "공휴일 운영 시작시각(HHMM)")
    private String lhldyBgng;

    @Schema(description = "공휴일 운영 종료시각(HHMM)")
    private String lhldy;
}
