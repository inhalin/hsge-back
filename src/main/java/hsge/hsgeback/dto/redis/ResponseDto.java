package hsge.hsgeback.dto.redis;

import lombok.Data;

@Data
public class ResponseDto {
    private String name;
    private Double lat;
    private Double lng;
    private Long userId;
}
