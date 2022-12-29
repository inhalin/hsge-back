package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.MessageRequestDto;
import hsge.hsgeback.dto.response.ChatResponseDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Message;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.chat.ChatroomRepository;
import hsge.hsgeback.repository.chat.MessageRepository;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat/message")
    public void message(MessageRequestDto message) {
        Long userId = message.getSenderId();
        Long roomId = message.getRoomId();
        String content = message.getMessage();
        User user = userRepository.findById(userId).orElseThrow();
        Chatroom chatroom = chatroomRepository.findById(roomId).orElseThrow();
        Message savedMessage = new Message(content, chatroom, user);
        messageRepository.save(savedMessage);

        ChatResponseDto payload = new ChatResponseDto(userId, roomId, content, savedMessage.getCreatedAt());

        template.convertAndSend("/sub/chat/room/" + roomId, payload);
    }

}
