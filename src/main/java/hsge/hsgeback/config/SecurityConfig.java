package hsge.hsgeback.config;

import hsge.hsgeback.repository.user.UserRepository;
import hsge.hsgeback.security.KakaoUserDetailService;
import hsge.hsgeback.security.filter.*;
import hsge.hsgeback.security.handler.LoginFailureHandler;
import hsge.hsgeback.security.handler.LoginSuccessHandler;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final WebClient webClient;
    private final KakaoUserDetailService kakaoUserDetailService;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    @Value("${report-count}")
    private int reportCount;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(PathRequest.toH2Console());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(kakaoUserDetailService)
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        http.addFilterBefore(new ExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class);

        LoginFilter loginFilter = new LoginFilter("/api/auth/login", webClient);
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtUtil));
        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());

        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(tokenCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new ReportCheckFilter(jwtUtil, userRepository, reportCount), TokenCheckFilter.class);
        http.addFilterAfter(new RefreshTokenFilter("/api/auth/refresh-token", jwtUtil), TokenCheckFilter.class);


        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/**", "/ws/**").permitAll()
                .anyRequest().denyAll();

        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil) {
        return new TokenCheckFilter(jwtUtil);
    }
}
