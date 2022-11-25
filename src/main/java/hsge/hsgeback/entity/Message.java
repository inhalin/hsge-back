package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private Chatroom chatroom;

    @ManyToOne
    private User userId;

    private Boolean checked;

    // 보내는 시간은 따로 필드값에 추가를 해줘야 되는지 아님 baseentity로 해두면 되는지 몰으겠읍미다

}
