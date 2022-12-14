package hsge.hsgeback.dto.response;

import lombok.Getter;

@Getter
public class SignupTokenResponseDto {

    private String accessToken;

    private String refreshToken;

    public SignupTokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
