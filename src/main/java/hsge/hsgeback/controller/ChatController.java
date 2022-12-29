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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        String email = jwtUtil.getEmail(request);
        return chatService.getChatMessages(email, roomId);
    }

    @PostMapping("/{roomId}/active")
    public void activeRoom(HttpServletRequest request, @PathVariable Long roomId) {
        String email = jwtUtil.getEmail(request);
        chatService.activeRoom(email, roomId);
    }
}
