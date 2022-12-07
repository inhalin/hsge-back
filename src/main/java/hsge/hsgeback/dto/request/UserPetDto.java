package hsge.hsgeback.dto.request;

import hsge.hsgeback.constant.Age;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserPetDto {

    //user
    private String nickname;

    private int profilePath;

    private Double latitude;

    private Double longtitude;

    private Double radius;

    //pet
    private String picture;

    private Boolean neutralization;

    private String likeTag;

    private String dislikeTag;

    private String description;

    private Age age;

}
