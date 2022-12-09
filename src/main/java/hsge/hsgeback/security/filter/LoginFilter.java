package hsge.hsgeback.security.filter;

import com.google.gson.Gson;
import hsge.hsgeback.dto.kakao.UserInfo;
import hsge.hsgeback.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final WebClient webClient;

    public LoginFilter(String defaultFilterProcessesUrl, WebClient webClient) {
        super(defaultFilterProcessesUrl);
        this.webClient = webClient;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (request.getMethod().equals("GET")) {
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }

        Map<String, String> jsonData = parseRequestJSON(request);

        String accessToken = jsonData.get("accessToken");
        String email = getEmail(accessToken);

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, "1111");

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private String getEmail(String accessToken) {
        UserInfo userInfo = webClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new TokenException("Access Token is not valid")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new TokenException("KaKao Server Internal Error")))
                .bodyToMono(UserInfo.class)
                .block();
        return userInfo.getEmail();
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        try (Reader reader = new InputStreamReader(request.getInputStream())) {

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
