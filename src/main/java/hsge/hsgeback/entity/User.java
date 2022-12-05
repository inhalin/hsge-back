package hsge.hsgeback.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
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

    private int profilePath;

    @ColumnDefault(value = "0.03")
    private Double radius;

    @OneToMany(mappedBy = "user")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "reporter")
    private List<Report> reporter;

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

    @Builder
    public User(Long id, String email, String nickname, String password, Double latitude, Double longtitude, String role, int profilePath, Double radius, List<Pet> pets, List<Report> reporter, List<Report> reportee, List<Match> matchList, List<Chatroom> likeUser, List<Chatroom> likedUser, List<Message> messageList) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.role = role;
        this.profilePath = profilePath;
        this.radius = radius;
        this.pets = pets;
        this.reporter = reporter;
        this.reportee = reportee;
        this.matchList = matchList;
        this.likeUser = likeUser;
        this.likedUser = likedUser;
        this.messageList = messageList;
    }

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
}
