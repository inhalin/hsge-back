package hsge.hsgeback.entity;

import lombok.AllArgsConstructor;
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

    @OneToMany(mappedBy = "reporter")
    private List<Report> reporter;

    @OneToMany(mappedBy = "reportee")
    private List<Report> reportee = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matchList = new ArrayList<>();

    @OneToMany(mappedBy = "likeUser")
    private List<Chatroom> likeUser;

    @OneToMany(mappedBy = "likedUser")
    private List<Chatroom> likedUser;

    @OneToMany(mappedBy = "user")
    private List<Message> messageList = new ArrayList<>();

    private String fcmToken;

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
