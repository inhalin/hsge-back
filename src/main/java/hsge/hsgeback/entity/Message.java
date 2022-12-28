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

    // 보내는 시간은 따로 필드값에 추가를 해줘야 되는지 아님 baseentity로 해두면 되는지 몰으겠읍미다

}
