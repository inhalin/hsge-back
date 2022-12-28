package hsge.hsgeback.dto.response;

import lombok.Data;

@Data
public class MessageUserInfoDto {

    private Long userId;

    private Long otherUserId;

    private String nickname;

    private int profilePath;

    public MessageUserInfoDto(Long userId, Long otherUserId, String nickname, int profilePath) {
        this.userId = userId;
        this.otherUserId = otherUserId;
        this.nickname = nickname;
        this.profilePath = profilePath;
    }
}
