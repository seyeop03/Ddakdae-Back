package idiots.ddakdae.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import idiots.ddakdae.domain.ParkingLot;
import lombok.Data;
import java.util.List;

@Data
public class ParkingLotResponse {
    @JsonProperty("DATA")
    private List<ParkingLot> data;
}

