package idiots.ddakdae.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import idiots.ddakdae.domain.Coordinates;
import lombok.RequiredArgsConstructor;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class GeocodeService {

    // VWorld API 키와 도메인
    private static final String API_KEY = "1CA725C9-1389-3EBE-A3DA-7C17E2DAFB2C";
    private static final String DOMAIN = "http://34.64.46.206:30081";
    private static final String API_URL = "http://apis.vworld.kr/new2coord.do";

    // 주소를 입력받아 위도와 경도를 반환하는 메인 메서드
    public Coordinates getCoordinatesFromAddress(String address) throws Exception {
        // 1. VWorld API 호출
        String url = buildApiUrl(address);
        String responseBody = callVWorldApi(url);

        // 2. 응답 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);

        // 응답에서 X, Y 좌표 추출
        double x = root.path("EPSG_900913_X").asDouble();
        double y = root.path("EPSG_900913_Y").asDouble();

        if (x == 0.0 || y == 0.0) {
            throw new IllegalStateException("검색 결과가 없습니다: " + address);
        }

        // 3. GeoTools로 좌표 변환
        return convertToLatLon(x, y);
    }

    // VWorld API URL 생성
    private String buildApiUrl(String address) throws Exception {
        return API_URL + "?q=" + URLEncoder.encode(address, "UTF-8") +
                "&apiKey=" + API_KEY + "&domain=" + DOMAIN + "&output=json&epsg=EPSG:900913";
    }

    // VWorld API 호출
    private String callVWorldApi(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API 호출 오류: 상태 코드 " + response.statusCode());
        }

        return response.body();
    }

    // EPSG:900913 좌표를 EPSG:4326(위도, 경도)로 변환
    private Coordinates convertToLatLon(double x, double y) throws FactoryException, TransformException {
        // 소스 및 타겟 좌표계 정의
        var sourceCRS = CRS.decode("EPSG:3857");
        var targetCRS = CRS.decode("EPSG:4326");

        // 좌표 변환
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(x, y));

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        Point transformedPoint = (Point) JTS.transform(point, transform);

        return new Coordinates(transformedPoint.getY(), transformedPoint.getX());
    }

}
