package hsge.hsgeback.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    private Chatroom chatroom;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Column(columnDefinition = "tinyint")
    private boolean checked;

    public Message(String content, Chatroom chatroom, User user) {
        this.content = content;
        this.chatroom = chatroom;
        this.user = user;
    }
}
