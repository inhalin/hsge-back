package hsge.hsgeback.security.filter;

import com.google.gson.Gson;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.UserRepository;
import hsge.hsgeback.util.JWTUtil;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class ReportCheckFilter extends OncePerRequestFilter {

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
}