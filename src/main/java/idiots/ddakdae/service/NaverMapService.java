package idiots.ddakdae.service;

// NaverMapService.java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverMapService {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
}

