package hsge.hsgeback.dto.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalkDto {
    private Double latitude;

    private Double longitude;

    private String nickname;

    private Long userId;

    @Builder
    public WalkDto(Double latitude, Double longitude, String nickname, Long userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.nickname = nickname;
        this.userId = userId;
    }
}
