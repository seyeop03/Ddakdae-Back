package idiots.ddakdae.dto.response.clustering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "거리 마커에서 주차장 목록 상세정보 DTO, 주차장 목록에서 사진 or 제목 클릭 시 상세정보")
public class NearbyParkingDetailDto {

    @Schema(description = "주차장 식별 ID")
    private Long plId;

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

    @Schema(description = "1시간 단위 기본 주차 요금")
    private Integer hourlyPrice;

    @Schema(description = "10분 당 추가 요금")
    private Integer addCrg10Mnt;

    @Schema(description = "월 정기권 금액")
    private Integer mntlCmutCrg;
}
