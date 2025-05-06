package idiots.ddakdae.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoGeoCoordinate {
    private String latitude;
    private String longitude;
}

