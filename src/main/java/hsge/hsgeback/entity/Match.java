package hsge.hsgeback.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "matching")
public class Match extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT")
    private Boolean likeValue;

    @ManyToOne(fetch = LAZY)
    private User user; // 좋아요 누른 사람

    @ManyToOne(fetch = LAZY)
    private Pet pet; // 좋아요 받은 강아지
}
