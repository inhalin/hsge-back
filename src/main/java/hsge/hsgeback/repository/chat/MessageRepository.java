package hsge.hsgeback.repository.chat;

import hsge.hsgeback.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
