package hsge.hsgeback.repository.chat;

import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.entity.Chatroom;

import java.util.List;

public interface ChatroomRepositoryCustom {

    List<ChatSimpleDto> findAllByMe(Long userId);

    List<ChatSimpleDto> findAllByOther(Long userId);

    Chatroom findByUserEmails(String user1Email, String user2Email);

    void updateActive(Long id);

    void leaveChatroom(Long roomId);
}
