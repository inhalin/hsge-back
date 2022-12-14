package hsge.hsgeback.exception;

import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException {

    TOKEN_ERROR tokenError;

    @Getter
    public enum TOKEN_ERROR {
        UNACCEPT(401, "Token is null or too short"),
        BADTYPE(401, "TOKEN type Bearer"),
        MALFORM(403, "Malformed Token"),
        BADSIGN(403, "BadSignatured Token"),
        EXPIRED(403, "Expired Token");

        private int status;
        private String message;

        TOKEN_ERROR(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    public AccessTokenException(TOKEN_ERROR error) {
        super(error.name());
        this.tokenError = error;
    }

    public void sendResponseError(HttpServletResponse response) {

        response.setStatus(tokenError.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("message", tokenError.getMessage(),
                "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
