package hsge.hsgeback.security.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private static final String NEED_SIGNUP = "NEED_SIGNUP";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        String message;
        if (exception instanceof BadCredentialsException) {
            message = NEED_SIGNUP;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message = exception.getMessage();
        }

        jsonObject.addProperty("message", message);

        String result = gson.toJson(jsonObject);

        response.getWriter().write(result);
    }
}
