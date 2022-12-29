package hsge.hsgeback.controller;

import hsge.hsgeback.dto.chat.ChatLeaveRequest;
import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.dto.common.BasicResponse;
import hsge.hsgeback.dto.response.MessageDto;
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

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<BasicResponse> leave(HttpServletRequest request, @PathVariable Long roomId, @RequestBody ChatLeaveRequest chatLeaveRequest) {
        return ResponseEntity.ok(chatService.leaveChatroom(jwtUtil.getEmail(request), roomId, chatLeaveRequest));
    }
}
