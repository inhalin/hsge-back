package hsge.hsgeback.entity;

import lombok.Builder;
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
    @ManyToOne
    private User reporter;

    @ManyToOne
    private User reportee;

    @Builder
    public Report(Long id, String description, User reporter, User reportee) {
        this.id = id;
        this.description = description;
        this.reporter = reporter;
        this.reportee = reportee;
    }
}
