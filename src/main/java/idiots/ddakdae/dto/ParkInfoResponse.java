package idiots.ddakdae.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkInfoResponse {

    @JsonProperty("GetParkInfo")
    private GetParkInfo getParkInfo;

    // Getter 및 Setter
    public GetParkInfo getGetParkInfo() {
        return getParkInfo;
    }

    public void setGetParkInfo(GetParkInfo getParkInfo) {
        this.getParkInfo = getParkInfo;
    }
}