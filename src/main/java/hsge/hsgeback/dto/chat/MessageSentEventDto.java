package hsge.hsgeback.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MessageSentEventDto {
    private String senderNickname;
    private int senderProfilePath;
    private String content;
    private List<String> receiverTokens;
    private Long roomId;
}
