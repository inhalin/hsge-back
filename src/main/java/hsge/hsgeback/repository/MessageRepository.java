package hsge.hsgeback.repository;

import hsge.hsgeback.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
