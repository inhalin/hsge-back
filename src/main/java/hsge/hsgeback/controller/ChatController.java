package hsge.hsgeback.controller;

import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.dto.response.MessageDto;
import hsge.hsgeback.dto.response.MessageResponseDto;
import hsge.hsgeback.dto.response.MessageUserInfoDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Message;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.chat.ChatroomRepository;
import hsge.hsgeback.service.ChatService;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final JWTUtil jwtUtil;
    private final ChatService chatService;
    private final ChatroomRepository chatroomRepository;

    @GetMapping
    public ResponseEntity<List<ChatSimpleDto>> list(HttpServletRequest request) {
        String email = jwtUtil.getEmail(request);

        return ResponseEntity.ok(chatService.findMyChats(email));
    }

    @GetMapping("/{roomId}")
    public MessageDto getChatMessages(HttpServletRequest request, @PathVariable Long roomId) {
        Chatroom chatroom = chatroomRepository.findById(roomId).orElseThrow();
        User likeUser = chatroom.getLikeUser();
        User likedUser = chatroom.getLikedUser();
        MessageUserInfoDto userInfo = new MessageUserInfoDto(likeUser.getId(), likedUser.getId(), likedUser.getNickname(), likedUser.getProfilePath());

        List<Message> messageList = chatroom.getMessageList();
        List<MessageResponseDto> result = messageList.stream()
                .map(m -> {
                    MessageResponseDto dto = new MessageResponseDto();
                    dto.setSenderId(m.getUser().getId());
                    dto.setMessage(m.getContent());
                    dto.setCreatedDate(m.getCreatedAt());
                    return dto;
                }).collect(Collectors.toList());

        return new MessageDto(userInfo, result);
    }
}
