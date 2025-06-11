package idiots.ddakdae.domain;

import idiots.ddakdae.dto.response.RealTimeParkingDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "pklt_real_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PkltRealTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long realTimeId;

    private String tpkct;

    private String nowPrkVhclCnt;

    private LocalDateTime nowPrkVhclCntUpdtTm;

    private String pkltCd;

    public PkltRealTime(RealTimeParkingDto dto) {
        this.pkltCd = dto.getPkltCd();
        this.tpkct = dto.getTpkct();
        this.nowPrkVhclCnt = dto.getNowPrkVhclCnt();
        this.nowPrkVhclCntUpdtTm = LocalDateTime.parse(dto.getNowPrkVhclCntUpdtTm());
    }
}
