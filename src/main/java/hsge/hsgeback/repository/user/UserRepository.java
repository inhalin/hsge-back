package hsge.hsgeback.repository.user;

import hsge.hsgeback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> deleteByEmail(String email);

    List<User> findByLatitudeBetweenAndLongitudeBetween(Double startLatitude, Double endLatitude, Double startLongitude, Double endLongitude);
}
