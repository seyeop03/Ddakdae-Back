package idiots.ddakdae.dto.request;

import lombok.Data;

@Data
public class NearbyParkingRequest {

    private double lat;
    private double lot;
    private int radius;
    private int page;
    private int size;
}
