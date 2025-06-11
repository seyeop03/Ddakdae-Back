package idiots.ddakdae.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import idiots.ddakdae.domain.PkltRealTime;
import idiots.ddakdae.repository.PkltRealTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ParkingDataScheduler {

    private static final int PAGE_SIZE = 1000;

    @Value("${REALTIME_PKLT_API}")
    private String API_KEY;

    private RestTemplate restTemplate;
    private PkltRealTimeRepository pkltRealTimeRepository;


    public ParkingDataScheduler(RestTemplate restTemplate, PkltRealTimeRepository pkltRealTimeRepository) {
        this.pkltRealTimeRepository = pkltRealTimeRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void fetchRealTimeData() throws JsonProcessingException {

        List<String> districts = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구",
            "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구",
            "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"
        );

        String base_url = "http://openapi.seoul.go.kr:8088/" + API_KEY + "/json/GetParkingInfo";

        for (String district : districts) {
            int start = 1;
            int end = PAGE_SIZE;

            while (true) {
                String url = String.format("%s/%d/%d/%s", base_url, start, end, district);
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                // JSON parsing
                JsonNode root = new ObjectMapper().readTree(response.getBody());
                JsonNode resultNode = root.get("GetParkingInfo");
                int totalCount = resultNode.get("list_total_count").asInt();
                JsonNode rows = resultNode.get("row");

                if (rows == null || !rows.isArray() || rows.size() == 0) break;

                for (JsonNode row : rows) {
                    log.info("row : {}", row);
                    String continueYN = row.get("PRK_STTS_YN").asText();
                    if (continueYN.equals("0")) continue; // 미연계중

                    String pkltCd = row.get("PKLT_CD").asText();
                    String tpkct = row.get("TPKCT").asText();
                    String nowPrkVhclCnt = row.get("NOW_PRK_VHCL_CNT").asText();
                    String updateTimeRaw = row.get("NOW_PRK_VHCL_UPDT_TM").asText();

                    log.info("API 주차장 코드: {}", pkltCd);
                    log.info("총 주차면: {}", tpkct);
                    log.info("현재 주차 차량 수: {}", nowPrkVhclCnt);
                    log.info("현재 주차 차량 수 업데이트 시간: {}", updateTimeRaw);

                    LocalDateTime updateTime = null;
                    if (!updateTimeRaw.equals("")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        updateTime = LocalDateTime.parse(updateTimeRaw, formatter);
                    }

                    Optional<PkltRealTime> existing = pkltRealTimeRepository.findByPkltCd(pkltCd);

                    if (existing.isPresent()) {
                        PkltRealTime entity = existing.get();
                        entity.setTpkct(tpkct);
                        entity.setNowPrkVhclCnt(nowPrkVhclCnt);
                        entity.setNowPrkVhclCntUpdtTm(updateTime);
                        pkltRealTimeRepository.save(entity);
                    } else {
                        PkltRealTime newEntity = new PkltRealTime();
                        newEntity.setPkltCd(pkltCd);
                        newEntity.setTpkct(tpkct);
                        newEntity.setNowPrkVhclCnt(nowPrkVhclCnt);
                        newEntity.setNowPrkVhclCntUpdtTm(updateTime);
                        pkltRealTimeRepository.save(newEntity);
                    }
                }

                start += PAGE_SIZE;
                end += PAGE_SIZE;

                if(start > totalCount) break;
            }
        }
    }
}
