package hsge.hsgeback.dto.request;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Slf4j
@Data
public class SignupDto {
    //user
    private Long userId;

    private String email;

    private String nickname;

    private Double latitude;

    private Double longtitude;

    private int profilePath;

    //pet
    private Long petId;

    private String petName;

    private String gender;

    private String description;

    private Boolean neutralization;

    private String Picture;

    @Enumerated(EnumType.STRING)
    private Age age;

    @Enumerated(EnumType.STRING)
    private Breed breed;

    private String likeTag; // 좋아요 태그

    private String dislikeTag; // 싫어요 태그

    @Builder
    public SignupDto(Long userId, String email, String nickname, Double latitude, Double longtitude, int profilePath, Long petId, String petName, String gender, String description, Boolean neutralization, String picture, Age age, Breed breed, String likeTag, String dislikeTag) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.profilePath = profilePath;
        this.petId = petId;
        this.petName = petName;
        this.gender = gender;
        this.description = description;
        this.neutralization = neutralization;
        this.Picture = picture;
        this.age = age;
        this.breed = breed;
        this.likeTag = likeTag;
        this.dislikeTag = dislikeTag;

    }

    public User toUserEntity() {
        log.info("nickname : {}",getNickname());
        return User.builder()
                .nickname(getNickname())
                .latitude(getLatitude())
                .longtitude(getLongtitude())
                .profilePath(getProfilePath())
                .email(getEmail())
                .build();
    }

    public Pet toPetEntity(User user) {
        return Pet.builder()
                .petName(getPetName())
                .gender(getGender())
                .description(getDescription())
                .neutralization(getNeutralization())
                .breed(getBreed())
                .likeTag(getLikeTag())
                .dislikeTag(getDislikeTag())
                .age(getAge())
                .user(user)
                .picture(getPicture())
                .build();
    }
}
