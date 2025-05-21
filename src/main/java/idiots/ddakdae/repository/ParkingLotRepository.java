package idiots.ddakdae.repository;

import idiots.ddakdae.domain.ParkingLot;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT p.gu, COUNT(p), AVG(p.lat), AVG(p.lot)
        FROM ParkingLot p
        WHERE p.lat BETWEEN :swLat AND :neLat AND p.lot BETWEEN :swLot AND :neLot
        GROUP BY p.gu
    """)
    List<?> groupByGu(double swLat, double neLat, double swLot, double neLot);

    @Query("""
        SELECT p.dong, COUNT(p), AVG(p.lat), AVG(p.lot)
        FROM ParkingLot p
        WHERE p.lat BETWEEN :swLat AND :neLat AND p.lot BETWEEN :swLot AND :neLot
        GROUP BY p.dong
    """)
    List<?> groupByDong(double swLat, double neLat, double swLot, double neLot);

    @Query("""
        select p
        FROM ParkingLot p
        WHERE p.lat BETWEEN :swLat AND :neLat AND p.lot BETWEEN :swLot AND :neLot
    """)
    List<?> getMarkers(double swLat, double neLat, double swLot, double neLot, Pageable pageable);
}
