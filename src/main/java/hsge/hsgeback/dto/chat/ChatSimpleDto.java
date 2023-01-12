package hsge.hsgeback.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSimpleDto {

    private Long roomId;
    private String nickname;
    private int profilePath;
    private String latestMessage;
    private Boolean checked;
    private Boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate firstDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime lastDate;
}
