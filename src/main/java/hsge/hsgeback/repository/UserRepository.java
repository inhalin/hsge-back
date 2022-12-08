package hsge.hsgeback.repository;

import hsge.hsgeback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> deleteByEmail(String email);

    List<User> findByLatitudeBetweenAndLongtitudeBetween(Double startLatitude, Double endLatitude, Double startLongtitude, Double endLongtitude);
}
