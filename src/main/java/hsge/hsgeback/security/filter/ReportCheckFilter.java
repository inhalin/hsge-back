package hsge.hsgeback.security.filter;

import com.google.gson.Gson;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.user.UserRepository;
import hsge.hsgeback.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class ReportCheckFilter extends OncePerRequestFilter {

    private final List<String> whitePathList = Arrays.asList(
            "/ws",
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/duplicate-nickname",
            "/api/auth/fcm/token",
            "/api/common"
    );
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final int reportLimit;

    public ReportCheckFilter(JWTUtil jwtUtil, UserRepository userRepository, int reportLimit) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.reportLimit = reportLimit;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIfWhitePath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getEmail(request);
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getReportCount() > reportLimit) {
            sendResponseError(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    public void sendResponseError(HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("message", "REPORT LIMIT EXCEED",
                "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkIfWhitePath(String path) {
        return whitePathList.stream().anyMatch(path::startsWith);
    }
}
