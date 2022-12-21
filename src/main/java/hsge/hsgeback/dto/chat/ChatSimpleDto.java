package hsge.hsgeback.dto.chat;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSimpleDto {
    private String nickname;
    private int profilePath;
    private String latestMessage;
    private Boolean checked;
    private Boolean active;
}
