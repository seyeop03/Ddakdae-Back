package idiots.ddakdae.domain;

import lombok.Data;

// 좌표 변환 결과 클래스
@Data
public class Coordinates {
    public double latitude;
    public double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "위도 (Latitude): " + latitude + ", 경도 (Longitude): " + longitude;
    }
}
