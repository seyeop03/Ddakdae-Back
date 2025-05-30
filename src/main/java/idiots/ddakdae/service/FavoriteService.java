package idiots.ddakdae.service;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.domain.Favorite;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.dto.response.clustering.NearbyParkingDto;
import idiots.ddakdae.repository.CustomerRepository;
import idiots.ddakdae.repository.FavoriteRepository;
import idiots.ddakdae.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CustomerRepository customerRepository;
    private final ParkingLotRepository parkingLotRepository;

    public void addFavorite(Long customerId, Long parkingLotId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("해당 사용자 없음"));
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new RuntimeException("해당 주차장 없음"));

        if (favoriteRepository.existsByCustomerAndParkingLot(customer, parkingLot)) {
            throw new IllegalArgumentException("이미 찜한 주차장입니다.");
        }

        favoriteRepository.save(
                Favorite.builder()
                        .customer(customer)
                        .parkingLot(parkingLot)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public void removeFavorite(Long customerId, Long parkingLotId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("해당 사용자 없음"));
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new RuntimeException("해당 주차장 없음"));

        Favorite favorite = favoriteRepository.findByCustomerAndParkingLot(customer, parkingLot)
                .orElseThrow(() -> new RuntimeException("해당 찜 내역 없음"));

        favoriteRepository.delete(favorite);
    }

    public List<NearbyParkingDto> getFavorites(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return favoriteRepository.findAllByCustomer(customer).stream()
                .map(fav -> {
                    ParkingLot pklt = fav.getParkingLot();
                    return NearbyParkingDto.builder()
                            .id(pklt.getPlId())
                            .pkltNm(pklt.getPkltNm())
                            .addr(pklt.getAddr())
                            .pkltKndNm(pklt.getPkltKndNm())
                            .tpkct(pklt.getTpkct())
                            .chgdFreeNm(pklt.getChgdFreeNm())
                            .wdOperBgngTm(pklt.getWdOperBgngTm())
                            .wdOperEndTm(pklt.getWdOperEndTm())
                            .weOperBgngTm(pklt.getWeOperBgngTm())
                            .weOperEndTm(pklt.getWeOperEndTm())
                            .lhldyBgng(pklt.getLhldyBgng())
                            .lhldy(pklt.getLhldy())
                            .build();
                })
                .toList();
    }
}
