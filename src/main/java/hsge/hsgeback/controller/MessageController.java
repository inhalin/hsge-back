package hsge.hsgeback.controller;

import hsge.hsgeback.dto.chat.MessageSentEventDto;
import hsge.hsgeback.dto.request.MessageRequestDto;
import hsge.hsgeback.dto.response.ChatResponseDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Message;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.event.chat.MessageSentEventPublisher;
import hsge.hsgeback.repository.chat.ChatroomRepository;
import hsge.hsgeback.repository.chat.MessageRepository;
import hsge.hsgeback.repository.user.UserRepository;
import hsge.hsgeback.repository.user.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;
    private final UserTokenRepository tokenRepository;
    private final MessageSentEventPublisher messageEventPublisher;

    @MessageMapping("/chat/message")
    public void message(MessageRequestDto message) {
        Long userId = message.getSenderId();
        Long roomId = message.getRoomId();
        String content = message.getMessage();
        User sender = userRepository.findById(userId).orElseThrow();
        User receiver = userRepository.findById(message.getReceiverId()).orElseThrow();
        List<String> tokens = tokenRepository.findTokenByEmail(receiver.getEmail());
        Chatroom chatroom = chatroomRepository.findById(roomId).orElseThrow();
        Message savedMessage = new Message(content, chatroom, sender);
        messageRepository.save(savedMessage);

        MessageSentEventDto eventDto = MessageSentEventDto.builder()
                .senderNickname(sender.getNickname())
                .senderProfilePath(sender.getProfilePath())
                .receiverTokens(tokens)
                .content(content)
                .roomId(roomId)
                .build();

        // 푸시 알림 이벤트 발행
        messageEventPublisher.publishEvent(eventDto);

        ChatResponseDto payload = new ChatResponseDto(userId, roomId, content, savedMessage.getCreatedAt());

        template.convertAndSend("/sub/chat/room/" + roomId, payload);
    }
}
