package com.web.jaru.certifications.dto;

import com.web.jaru.certifications.domain.ScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class CertScheduleDTO {

    @Schema(name = "내 자격증 일정 응답")
    public record MyCertScheduleResponse(
            @Schema(description = "자격증 ID", example = "1")
            Long certId,

            @Schema(description = "북마크 여부", example = "true")
            boolean isBookmarked,

            @Schema(description = "알림 설정 여부", example = "false")
            boolean isAlarmed,

            @Schema(description = "일정명", example = "2025년 정기 기사 1회 필기시험")
            String scheduleName,

            @Schema(description = "일정 타입 (접수, 시험)", example = "EXAM")
            ScheduleType scheduleType,

            @Schema(description = "일정 날짜", example = "2025-03-12")
            LocalDate scheduleDate,

            @Schema(description = "일정 요일", example = "수요일")
            String scheduleDay
    ){}

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
