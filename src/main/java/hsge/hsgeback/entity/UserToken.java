package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class UserToken {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    @Column(unique = true)
    private String token;

    public UserToken(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public static UserToken of (String email, String token) {
        return new UserToken(email, token);
    }
}
