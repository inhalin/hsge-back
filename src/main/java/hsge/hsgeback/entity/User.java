package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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
    private List<Pet> pet = new ArrayList<>();

}
