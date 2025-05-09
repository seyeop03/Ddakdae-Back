package idiots.ddakdae.service;

// NaverMapService.java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import idiots.ddakdae.dto.response.NaverApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverMapService {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    @Value("${naver.search.client-id}")
    private String clientIdV2;

    @Value("${naver.search.client-secret}")
    private String clientSecretV2;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String getCoordinates(String address) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode addresses = root.path("addresses");
                if (addresses.isArray() && addresses.size() > 0) {
                    JsonNode firstAddress = addresses.get(0);
                    String x = firstAddress.path("x").asText(); // 경도
                    String y = firstAddress.path("y").asText(); // 위도
                    return "Latitude: " + y + ", Longitude: " + x;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "No coordinates found";
    }

    public Object searchLocal(String query, int display, int start, String sort) {
        String url = UriComponentsBuilder.fromHttpUrl("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", query)
                .queryParam("display", display)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", clientIdV2);
        headers.add("X-Naver-Client-Secret", clientSecretV2);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<NaverApiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                NaverApiResponse.class
        );


        String mapx = response.getBody().getItems().get(0).mapx;
        String mapy = response.getBody().getItems().get(0).mapy;

        System.out.println(mapx + ", " + mapy);

        return response.getBody();
    }
}

