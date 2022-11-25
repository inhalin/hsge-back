package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gender;

    private String description;

    private Boolean neutralization;

    private String picture;

    private String breed;

    @ManyToOne
    private User user;

//    @OneToMany(mappedBy = "petId")
//    private List<Match> match;


    // 태그 작업 추가로 필요함미다.
    private String like; // 좋아요 태그

    private String dislike; // 싫어요 태그
}
