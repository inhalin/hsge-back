package hsge.hsgeback.repository;

import hsge.hsgeback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
