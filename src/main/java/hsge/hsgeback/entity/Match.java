package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "matching")
public class Match extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean likeValue;

    private Boolean accept;

    @ManyToOne
    private User user; // 좋아요 누른 사람

    @ManyToOne
    private Pet pet; // 좋아요 받은 강아지


}
