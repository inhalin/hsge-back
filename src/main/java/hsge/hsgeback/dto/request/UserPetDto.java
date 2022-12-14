package hsge.hsgeback.dto.request;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
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

    private String town;

    //pet
    private String petName;

    private Age age;

    private String gender;

    private String description;

    private Boolean neutralization;

    private String picture;

    private Breed breed;

    private String likeTag;

    private String dislikeTag;

}
