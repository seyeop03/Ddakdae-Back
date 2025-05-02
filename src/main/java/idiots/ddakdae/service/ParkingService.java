package idiots.ddakdae.service;

import idiots.ddakdae.dto.ParkInfo;
import idiots.ddakdae.dto.ParkInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_KEY = "514d5766676c736a34374c6a6a7457";
    // PORT 번호 없앴다 🔥
    private static final String BASE_URL = "https://openapi.seoul.go.kr";

    public String getParkingInfoRaw(int start, int end) {
        String url = String.format("%s/%s/json/GetParkInfo/%d/%d/", BASE_URL, API_KEY, start, end);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}


