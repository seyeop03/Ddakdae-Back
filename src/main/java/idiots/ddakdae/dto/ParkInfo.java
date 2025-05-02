package idiots.ddakdae.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkInfo {

    @JsonProperty("PARKING_NAME")
    private String parkingName;

    @JsonProperty("ADDR")
    private String address;

    @JsonProperty("CAPACITY")
    private int capacity;

    @JsonProperty("CUR_PARKING")
    private int currentParking;

    // 기타 필드들에 대한 매핑 추가

    // Getter 및 Setter
    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentParking() {
        return currentParking;
    }

    public void setCurrentParking(int currentParking) {
        this.currentParking = currentParking;
    }
}
