package hsge.hsgeback.dto.kakao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class KakaoUser extends User {

    private String email;
    private String password;

    public KakaoUser(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.email = email;
        this.password = password;
    }
}
