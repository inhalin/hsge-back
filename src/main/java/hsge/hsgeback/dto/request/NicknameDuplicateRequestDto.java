package hsge.hsgeback.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameDuplicateRequestDto {

    private String nickname;

    public NicknameDuplicateRequestDto(String nickname) {
        this.nickname = nickname;
    }

}
