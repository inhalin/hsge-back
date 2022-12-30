package hsge.hsgeback.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = LAZY)
    private User likeUser;

    @ManyToOne(fetch = LAZY)
    private User likedUser;

    @Column(columnDefinition = "tinyint")
    private Boolean active;

    private LocalDateTime leftAt;

    @OneToMany(mappedBy = "chatroom", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();

    public void activeChatroom() {
        this.active = true;
    }
}
