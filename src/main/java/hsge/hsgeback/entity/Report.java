package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Report extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // 연관관계 매핑
    @OneToOne
    private User reporterId;

    @OneToOne
    private User reporteeId;


}
