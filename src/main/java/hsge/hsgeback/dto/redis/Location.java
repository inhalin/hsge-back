package hsge.hsgeback.dto.redis;

import lombok.Data;

@Data
public class Location {
    private String nickname;
    private String name;
    private Double lat;
    private Double lng;
}
