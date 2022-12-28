package hsge.hsgeback.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatResponseDto {

    private Long senderId;

    private Long roomId;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    public ChatResponseDto(Long senderId, Long roomId, String message, LocalDateTime createdDate) {
        this.senderId = senderId;
        this.roomId = roomId;
        this.message = message;
        this.createdDate = createdDate;
    }
}
