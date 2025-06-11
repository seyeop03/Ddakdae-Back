package idiots.ddakdae.repository;

import idiots.ddakdae.domain.PkltRealTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PkltRealTimeRepository extends JpaRepository<PkltRealTime, Long> {

    Optional<PkltRealTime> findByPkltCd(String pkltCd);

    @Query(value = """
        SELECT
            p.pl_id,
            p.pklt_nm,
            r.tpkct,
            r.now_prk_vhcl_cnt,
            r.now_prk_vhcl_cnt_updt_tm
        FROM pklt_real_time r
        INNER JOIN parking_lot p ON r.pklt_cd = p.pklt_cd
        WHERE r.now_prk_vhcl_cnt_updt_tm is not null
    """, nativeQuery = true)
    List<Map<String, Object>> findAllRealtimeWithParkingLot();

}
