package hsge.hsgeback.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {

    private Long senderId;
    private Long roomId;
    private String message;
}
