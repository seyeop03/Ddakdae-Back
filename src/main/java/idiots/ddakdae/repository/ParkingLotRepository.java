package idiots.ddakdae.repository;

import idiots.ddakdae.domain.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    @Query(value = "SELECT *, (6371 * acos(cos(radians(:lat)) * cos(radians(lat)) * " +
            "cos(radians(lot) - radians(:lon)) + sin(radians(:lat)) * sin(radians(lat)))) AS distance " +
            "FROM parking_lot " +
            "HAVING distance < :radius " +
            "ORDER BY distance", nativeQuery = true)
    List<ParkingLot> findParkingLotsWithinRadius(@Param("lat") double lat,
                                                 @Param("lon") double lon,
                                                 @Param("radius") double radius);
}
