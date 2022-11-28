package hsge.hsgeback.dto.kakao;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Properties;

@Data
public class UserInfo {

    private Long id;
    private LocalDateTime connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    static class Properties {
        private String nickname;
    }

    @Data
    static class KakaoAccount {
        private String email;
    }

    public String getEmail() {
        return kakao_account.getEmail();
    }
}