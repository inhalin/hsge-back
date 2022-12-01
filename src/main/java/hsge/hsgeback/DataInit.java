package hsge.hsgeback;

import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Profile("dev")
@Component
public class DataInit {

    private final UserRepository userRepository;

    @PostConstruct
    void init() {

        log.info("dump data init when profile dev");

        User user1 = User.builder().email("somefood@naver.com")
                .nickname("홍석주")
                .build();

        User user2 = User.builder().email("syb8200@naver.com")
                .nickname("신예빈")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
    }
}
