package hsge.hsgeback.security;

import hsge.hsgeback.dto.kakao.KakaoUser;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoUserDetailService implements UserDetailsService {

    @Value("${report-count}")
    private int reportLimit;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("KakaoUserDetailService email: {}", email);
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        if (findUser.getReportCount() > reportLimit) {
            throw new AuthenticationServiceException("REPORT LIMIT EXCEED");
        }
        return new KakaoUser(email, passwordEncoder.encode("1111"), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
