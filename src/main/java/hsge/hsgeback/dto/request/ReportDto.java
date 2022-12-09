package hsge.hsgeback.dto.request;

import hsge.hsgeback.entity.Report;
import hsge.hsgeback.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDto {

    private Long reportee;

    private String description;

    public ReportDto() {
    }

    @Builder
    public ReportDto(Long reportee, String description) {
        this.reportee = reportee;
        this.description = description;
    }

    public Report toReportEntity(User reporter, User reportee) {
        return Report.builder()
                .reportee(reportee)
                .reporter(reporter)
                .description(getDescription())
                .build();
    }
}
