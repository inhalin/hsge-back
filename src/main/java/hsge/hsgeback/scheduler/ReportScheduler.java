package hsge.hsgeback.scheduler;

import hsge.hsgeback.entity.Report;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.ReportRepository;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ReportScheduler {

    private final ReportRepository reportRepository;

    private final UserRepository userRepository;

//    @Scheduled(cron = "40 * * * * *")
//    public void run() {
//        log.info("all=================================================");
//        List<User> all = userRepository.findAll();
//        for (User user : all) {
//            log.info("user의 신고사항: {} report start", user.getId());
//            List<Report> report = reportRepository.findByReportee(user);
//            log.info("user의 신고사항: {} report end", user.getId());
//
//            log.info("user의 신고 사이즈 size start");
//            int size = report.size();
//            log.info("{}", size);
//            log.info("user의 신고 사이즈 size end");
//
//            user.setReportCount(size);
//        }
//
//    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void run2() {
        List<User> all = userRepository.findAll();
        for (User user : all) {
            int result = reportRepository.countByReportee(user);
            user.setReportCount(result);
        }

    }
}
