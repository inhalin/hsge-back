package hsge.hsgeback.dto.kakao;

import lombok.Data;

@Data
public class TokenInfo {
        private Long expiresInMillis;
        private Long id;
        private Long expires_in;
        private Long app_id;
        private Long appId;
}
