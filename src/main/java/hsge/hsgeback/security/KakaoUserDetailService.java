package hsge.hsgeback.security;

import hsge.hsgeback.dto.kakao.KakaoUser;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("KakaoUserDetailService email: {}", email);
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            log.info("해당 유저 찾을 수 없음");
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }
        return new KakaoUser(email, passwordEncoder.encode("1111"), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
