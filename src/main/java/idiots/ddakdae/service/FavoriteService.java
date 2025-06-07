package idiots.ddakdae.service;

import idiots.ddakdae.domain.Customer;
import idiots.ddakdae.domain.Favorite;
import idiots.ddakdae.domain.ParkingLot;
import idiots.ddakdae.dto.response.clustering.NearbyParkingDto;
import idiots.ddakdae.exception.BizException;
import idiots.ddakdae.exception.ErrorCode;
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
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_PKLT));

        if (favoriteRepository.existsByCustomerAndParkingLot(customer, parkingLot)) {
            throw new BizException(ErrorCode.EXIST_PKLT_FAVORITE);
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
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_PKLT));

        Favorite favorite = favoriteRepository.findByCustomerAndParkingLot(customer, parkingLot)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_FAVORITE));

        favoriteRepository.delete(favorite);
    }

    public List<NearbyParkingDto> getFavorites(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));

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
