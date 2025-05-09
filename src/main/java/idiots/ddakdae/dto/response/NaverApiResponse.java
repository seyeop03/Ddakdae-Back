package idiots.ddakdae.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class NaverApiResponse {

    private List<NaverApiItem> items;
}
