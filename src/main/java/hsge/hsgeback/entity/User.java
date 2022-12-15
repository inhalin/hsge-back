package hsge.hsgeback.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
@AllArgsConstructor
@Builder
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private Double latitude;

    private Double longtitude;

    private String role;

    private String town;

    private int profilePath;

    @ColumnDefault(value = "0.03")
    private Double radius;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "reporter")
    private List<Report> reporter;

    @Builder.Default
    @OneToMany(mappedBy = "reportee")
    private List<Report> reportee;

    @OneToMany(mappedBy = "user")
    private List<Match> matchList = new ArrayList<>();

    @OneToMany(mappedBy = "likeUser")
    private List<Chatroom> likeUser;

    @OneToMany(mappedBy = "likedUser")
    private List<Chatroom> likedUser;

    @OneToMany(mappedBy = "userId")
    private List<Message> messageList = new ArrayList<>();

    private String fcmToken;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public void setProfilePath(int profilePath) {
        this.profilePath = profilePath;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
