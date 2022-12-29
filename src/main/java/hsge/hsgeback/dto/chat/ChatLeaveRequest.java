package hsge.hsgeback.dto.chat;

import hsge.hsgeback.entity.ChatLeaveType;
import lombok.Getter;

@Getter
public class ChatLeaveRequest {
    private ChatLeaveType type;
    private Long counterUserId;
}
