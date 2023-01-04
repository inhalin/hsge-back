package hsge.hsgeback.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private Double latitude;

    private Double longitude;

    private String role;

    private String town;

    private int profilePath;

    private int reportCount;

    @ColumnDefault("true")
    private Boolean isValid = true;

    @ColumnDefault(value = "0.03")
    private Double radius;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reporter;

    @OneToMany(mappedBy = "reportee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reportee = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matchList = new ArrayList<>();

    @OneToMany(mappedBy = "likeUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chatroom> likeUser = new ArrayList<>();

    @OneToMany(mappedBy = "likedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chatroom> likedUser = new ArrayList<>();

    @Builder
    public User(Long id, String email, String nickname, String password, Double latitude, Double longitude, String role, String town, int profilePath, int reportCount, Boolean isValid, Double radius, List<Pet> pets, List<Report> reporter, List<Report> reportee, List<Match> matchList, List<Chatroom> likeUser, List<Chatroom> likedUser) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.role = role;
        this.town = town;
        this.profilePath = profilePath;
        this.reportCount = reportCount;
        this.isValid = isValid;
        this.radius = radius;
        this.pets = pets;
        this.reporter = reporter;
        this.reportee = reportee;
        this.matchList = matchList;
        this.likeUser = likeUser;
        this.likedUser = likedUser;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }


    @Transient
    private Double startLatitude;

    @Transient
    private Double endLatitude;

    @Transient
    private Double startLongitude;

    @Transient
    private Double endLongitude;

    public void calculateLocation() {
        this.startLatitude = this.latitude - this.radius;
        this.endLatitude = this.latitude + this.radius;
        this.startLongitude = this.longitude - this.radius;
        this.endLongitude = this.longitude + this.radius;
    }
}
