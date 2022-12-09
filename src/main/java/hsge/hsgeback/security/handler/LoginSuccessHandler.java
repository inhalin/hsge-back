package hsge.hsgeback.security.handler;

import com.google.gson.Gson;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 JWT 토큰을 발급해 드리겠습니다.");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> claim = Map.of("email", authentication.getName());
        String accessToken = jwtUtil.generateAccessToken(claim);
        String refreshToken = jwtUtil.generateRefreshToken(claim);

        Gson gson = new Gson();

        Map<String, String> keyMap = Map.of(
                "message", "LOGIN",
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);

    }
}
