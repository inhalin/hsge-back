package hsge.hsgeback.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class BasicResponse {
    private Long targetId;
    private String message;
}
