package hsge.hsgeback.dto.redis;

import lombok.Data;

@Data
public class LocationDto {
    private String nickname;
    private String name;
    private Double lat;
    private Double lng;
}
