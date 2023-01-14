package hsge.hsgeback.scheduler;


import hsge.hsgeback.repository.match.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CardScheduler {
    private final MatchRepository matchRepository;

    @Transactional
    @Scheduled(cron = "0 0 0/4 * * *")
    public void run(){
        matchRepository.deleteAllByLikeValue(false);
    }


}
