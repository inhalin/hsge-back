package hsge.hsgeback.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
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
    
    private String profilePath;

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
    public User(String email, String nickname, String password, Double latitude, Double longtitude, String role, String profilePath, List<Pet> pet, Report reporter, Report reportee, List<Match> matchList, List<Chatroom> likeUser, List<Chatroom> likedUser, List<Message> messageList) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.profilePath = profilePath;
    }
}
