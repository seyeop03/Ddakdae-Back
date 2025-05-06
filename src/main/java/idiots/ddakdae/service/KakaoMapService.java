package idiots.ddakdae.service;

import idiots.ddakdae.domain.Coordinates;
import idiots.ddakdae.domain.KakaoGeoCoordinate;
import idiots.ddakdae.domain.KakaoGeoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoMapService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;  // application.yml 또는 .properties에 저장

    private final RestTemplate restTemplate;

    public Optional<KakaoGeoCoordinate> getCoordinatesByRoadAddress(String address) {

        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedAddress + "&analyze_type=similar";;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Object.class
        );

        System.out.println(response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());


        }
        return Optional.empty();
    }
}

