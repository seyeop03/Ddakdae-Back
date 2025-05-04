package idiots.ddakdae.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "parking_lot")
public class ParkingLot {

    /** 내부 식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plId;

    /** 주차장명 */
    private String pkltNm;

    /** 주소 */
    private String addr;

    /** 주차장코드 */
    private String pkltCd;

    /** 주차장 종류 */
    private String pkltKnd;

    /** 주차장 종류명 */
    private String pkltKndNm;

    /** 운영구분 */
    private String operSe;

    /** 운영구분명 */
    private String operSeNm;

    /** 전화번호 */
    private String telno;

    /** 주차현황 정보 제공여부 */
    private String prkNowInfoPvsnYn;

    /** 주차현황 정보 제공여부명 */
    private String prkNowInfoPvsnYnNm;

    /** 총 주차면 */
    private Integer tpkct;

    /** 유무료구분 */
    private String chgdFreeSe;

    /** 유무료구분명 */
    private String chgdFreeNm;

    /** 야간무료개방여부 */
    private String nghtFreeOpnYn;

    /** 야간무료개방여부명 */
    private String nghtFreeOpnYnName;

    /** 평일 운영 시작시각(HHMM) */
    private String wdOperBgngTm;

    /** 평일 운영 종료시각(HHMM) */
    private String wdOperEndTm;

    /** 주말 운영 시작시각(HHMM) */
    private String weOperBgngTm;

    /** 주말 운영 종료시각(HHMM) */
    private String weOperEndTm;

    /** 공휴일 운영 시작시각(HHMM) */
    private String lhldyBgng;

    /** 공휴일 운영 종료시각(HHMM) */
    private String lhldy;

    /** 최종데이터 동기화 시간 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastDataSyncTm;

    /** 토요일 유,무료 구분 */
    private String satChgdFreeSe;

    /** 토요일 유,무료 구분명 */
    private String satChgdFreeNm;

    /** 공휴일 유,무료 구분 */
    private String lhldyYn;

    /** 공휴일 유,무료 구분명 */
    private String lhldyNm;

    /** 월 정기권 금액 */
    private Integer mntlCmutCrg;

    /** 노상 주차장 관리그룹번호 */
    private String crbPkltMngGroupNo;

    /** 기본 주차 요금 */
    private Integer prkCrg;

    /** 기본 주차 시간(분 단위) */
    private Integer prkHm;

    /** 추가 단위 요금 */
    private Integer addCrg;

    /** 추가 단위 시간(분 단위) */
    private Integer addUnitTmMnt;

    /** 버스 기본 주차 요금 */
    private Integer busPrkCrg;

    /** 버스 기본 주차 시간(분 단위) */
    private Integer busPrkHm;

    /** 버스 추가 단위 시간(분 단위) */
    private Integer busPrkAddHm;

    /** 버스 추가 단위 요금 */
    private Integer busPrkAddCrg;

    /** 일 최대 요금 */
    private Integer dlyMaxCrg;

    /** 위도 */
    private BigDecimal lat;

    /** 경도 */
    private BigDecimal lot;
}
