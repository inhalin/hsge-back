package hsge.hsgeback.controller;

import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.service.ChatService;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
