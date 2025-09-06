package com.web.jaru.certifications.dto;

import com.web.jaru.certifications.domain.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

public class CertScheduleDTO {

    @Getter
    public static class MyCertScheduleResponse {

        @Schema(description = "자격증 ID", example = "101")
        private Long certId;

        @Schema(description = "사용자가 북마크했는지 여부", example = "true")
        private boolean isBookmarked;

        @Schema(description = "사용자가 알림 설정했는지 여부", example = "false")
        private boolean isAlarmed;

        @Schema(description = "실제 일정 날짜 (접수 시작일, 시험일, 발표일)", example = "2025-09-15")
        private LocalDate scheduleDate;

        @Schema(description = "일정 요일", example = "MONDAY")
        private String dayOfWeek;

        @Schema(description = "일정 제목 (자격증명 + 일정명)",
                example = "정보처리기사 원서접수")
        private String title;

        public MyCertScheduleResponse(Long certId,
                                      boolean isBookmarked,
                                      boolean isAlarmed,
                                      LocalDate applyFrom,
                                      LocalDate testDateFrom,
                                      LocalDate resultDate,
                                      String certName) {
            this.certId = certId;
            this.isBookmarked = isBookmarked;
            this.isAlarmed = isAlarmed;

            if (applyFrom != null) {
                this.scheduleDate = applyFrom;
                this.dayOfWeek = applyFrom.getDayOfWeek().toString();
                this.title = certName + " 원서접수";
            } else if (testDateFrom != null) {
                this.scheduleDate = testDateFrom;
                this.dayOfWeek = testDateFrom.getDayOfWeek().toString();
                this.title = certName + " 시험";
            } else if (resultDate != null) {
                this.scheduleDate = resultDate;
                this.dayOfWeek = resultDate.getDayOfWeek().toString();
                this.title = certName + " 합격자 발표";
            } else {
                this.scheduleDate = null;
                this.dayOfWeek = null;
                this.title = certName + " 일정";
            }
        }
    }

    @Schema(name = "자격증 일정 응답")
    public record CertScheduleResponse(
            @Schema(description = "자격증 ID", example = "2")
            Long certId,

            @Schema(description = "일정명", example = "2025년 정기 기사 2회 접수 시작")
            String scheduleName,

            @Schema(description = "일정 타입", example = "APPLY")
            ScheduleType scheduleType,

            @Schema(description = "일정 날짜", example = "2025-05-01")
            LocalDate scheduleDate,

            @Schema(description = "일정 요일", example = "목요일")
            String scheduleDay
    ){}

    @Schema(name = "월별 자격증 일정 응답")
    public record MonthlyCertResponse(
            @Schema(description = "자격증 ID", example = "3")
            Long certId,

            @Schema(description = "일정명", example = "2025년 정기 기사 3회 필기시험")
            String scheduleName,

            @Schema(description = "카테고리 ID", example = "10")
            Long categoryId,

            @Schema(description = "카테고리명", example = "IT/컴퓨터")
            String categoryName,

            @Schema(description = "접수 시작일", example = "2025-07-01")
            LocalDate applyFrom,

            @Schema(description = "접수 종료일", example = "2025-07-07")
            LocalDate applyTo
    ){}
}
