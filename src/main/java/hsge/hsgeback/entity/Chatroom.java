package hsge.hsgeback.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
@Builder
public class Chatroom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User likeUser;

    @ManyToOne
    private User likedUser;

    @Column(columnDefinition = "tinyint")
    private Boolean active;

    @OneToMany(mappedBy = "chatroom", fetch = LAZY)
    private List<Message> messageList = new ArrayList<>();
}
