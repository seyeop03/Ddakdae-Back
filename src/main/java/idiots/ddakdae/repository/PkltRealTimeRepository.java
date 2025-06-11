package idiots.ddakdae.repository;

import idiots.ddakdae.domain.PkltRealTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PkltRealTimeRepository extends JpaRepository<PkltRealTime, Long> {

    Optional<PkltRealTime> findByPkltCd(String pkltCd);
}
