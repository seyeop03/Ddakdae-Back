package idiots.ddakdae.dto.response;

import lombok.Data;

@Data
public class NaverApiItem {

    public String title;
    public String link;
    public String category;
    public String description;
    public String telephone;
    public String address;
    public String roadAddress;
    public double mapx;
    public double mapy;

    public double getMapx() {
        return mapx / 1e7;
    }

    public double getMapy() {
        return mapy / 1e7;
    }
}
