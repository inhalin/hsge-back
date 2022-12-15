package hsge.hsgeback.entity;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String petName;

    @Enumerated(EnumType.STRING)
    private Age age;

    private String gender;

    private String description;

    private Boolean neutralization;

    @OneToMany(mappedBy = "pet",  cascade = CascadeType.ALL)
    private List<PetImg> petImg = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Breed breed;

    private String likeTag; // 좋아요 태그

    private String dislikeTag; // 싫어요 태그

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "pet")
    private final List<Match> matchList = new ArrayList<>();

    @Builder
    public Pet(Long id, String petName, Age age, String gender, String description, Boolean neutralization, List<PetImg> petImg, Breed breed, String likeTag, String dislikeTag, User user) {
        this.id = id;
        this.petName = petName;
        this.age = age;
        this.gender = gender;
        this.description = description;
        this.neutralization = neutralization;
        this.petImg = petImg;
        this.breed = breed;
        this.likeTag = likeTag;
        this.dislikeTag = dislikeTag;
        this.user = user;
    }


    public void updatePet(String petName, String gender, Breed breed, Boolean neutralization, String likeTag, String dislikeTag, String description, Age age){
        this.neutralization = neutralization;
        this.likeTag = likeTag;
        this.dislikeTag = dislikeTag;
        this.description = description;
        this.age = age;
        this.breed = breed;
        this.gender = gender;
        this.petName = petName;
    }

    public void setPetImg(List<PetImg> petImg) {
        this.petImg = petImg;
    }
}
