package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Match extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean likeValue;

    private Boolean accept;

    @ManyToOne
    private User userId; // 좋아요 받은 강아지

    @ManyToOne
    private Pet petId; // 좋아요 누른 사람


}
