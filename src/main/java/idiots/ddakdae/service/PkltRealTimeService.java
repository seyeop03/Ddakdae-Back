package idiots.ddakdae.service;

import idiots.ddakdae.dto.response.RealTimeParkingResponseDto;
import idiots.ddakdae.repository.PkltRealTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PkltRealTimeService {

    private final PkltRealTimeRepository pkltRealTimeRepository;

    public List<RealTimeParkingResponseDto> getAll() {
        List<Map<String, Object>> rawList = pkltRealTimeRepository.findAllRealtimeWithParkingLot();

        return rawList.stream().map(row -> new RealTimeParkingResponseDto(
                ((Number) row.get("plId")).longValue(),
                (String) row.get("pklt_nm"),
                (int) Double.parseDouble((String) row.get("tpkct")),
                (int) Double.parseDouble((String) row.get("nowPrkVhclCnt")),
                (Timestamp) row.get("nowPrkVhclCntUpdtTm")
        )).toList();
    }
}
