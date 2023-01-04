package hsge.hsgeback.repository.user;

import hsge.hsgeback.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    boolean existsByEmailAndToken(String email, String token);

    void deleteByEmailAndToken(String email, String token);
}
