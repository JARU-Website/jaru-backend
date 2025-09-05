package com.web.jaru.certifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class CertDTO {

    public record CertResponse(

    ){}

    public record CertDetailResponse(

            @Schema(description = "자격증 ID", example = "1")
            Long certId,

            @Schema(description = "자격증명", example = "정보처리기사")
            String certName,

            @Schema(description = "시행기관", example = "한국산업인력공단")
            String issuer,

            @Schema(description = "자격 구분", example = "국가기술자격")
            String certType,

            @Schema(description = "공식 사이트 URL", example = "https://www.q-net.or.kr")
            String officialUrl,

            @Schema(description = "시험 개요", example = "소프트웨어 개발 능력을 검증하는 국가기술자격 시험")
            String testOverview,

            @Schema(description = "난이도 (1~5)", example = "3")
            Short difficulty,

            @Schema(description = "시험 내용", example = "필기/실기 시험으로 구성")
            String testContent,

            @Schema(description = "합격 기준", example = "60점 이상")
            String passingScore,

            Long categoryId,

            @Schema(description = "카테고리명", example = "IT/컴퓨터")
            String categoryName

    ){}

    public record PopularCertResponse(
            @Schema(description = "자격증 ID", example = "1")
            Long certId,
            @Schema(description = "자격증명", example = "정보처리기사")
            String certName,
            @Schema(description = "접수 시작일", example = "2025-07-01")
            LocalDate applyFrom,

            @Schema(description = "접수 종료일", example = "2025-07-07")
            LocalDate applyTo

    ){}

}
