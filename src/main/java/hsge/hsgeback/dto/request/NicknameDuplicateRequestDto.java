package hsge.hsgeback.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NicknameDuplicateRequestDto {

    private String nickname;

    public NicknameDuplicateRequestDto(String nickname) {
        this.nickname = nickname;
    }

}
