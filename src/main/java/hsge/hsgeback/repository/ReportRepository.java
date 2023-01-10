package hsge.hsgeback.repository;

import hsge.hsgeback.entity.Report;
import hsge.hsgeback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query(value = "select r from Report r where r.reporter =:reporter")
    List<Report> findReportee(@Param(value = "reporter") User reporter);

    List<Report> findByReportee(User user);

    int countByReportee(User user);
}
