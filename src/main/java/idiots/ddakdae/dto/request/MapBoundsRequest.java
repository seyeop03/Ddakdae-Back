package idiots.ddakdae.dto.request;

import lombok.Data;

@Data
public class MapBoundsRequest {

    private double swLat;
    private double swLot;
    private double neLat;
    private double neLot;
    private int zoomLevel;
}
