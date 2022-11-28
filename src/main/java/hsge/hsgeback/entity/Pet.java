package hsge.hsgeback.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String petName;

    private String gender;

    private String description;

    private Boolean neutralization;

    private String picture;

    private String breed;

    private String likeTag; // 좋아요 태그

    private String dislikeTag; // 싫어요 태그

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "pet")
    private final List<Match> matchList = new ArrayList<>();


    @Builder
    public Pet(String petName, String gender, String description, Boolean neutralization, String breed, String picture, User user, String likeTag, String dislikeTag){
        this.petName = petName;
        this.gender = gender;
        this.description = description;
        this.neutralization = neutralization;
        this.breed = breed;
        this.picture = picture;
        this.likeTag = likeTag;
        this.dislikeTag = dislikeTag;
    }
}
