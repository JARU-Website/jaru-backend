package com.web.jaru.certifications.dto;

import com.web.jaru.certifications.domain.Certification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

            @Schema(description = "난이도 (1~5)", example = "3")
            Short difficulty,

            @Schema(description = "시험 내용", example = "필기/실기 시험으로 구성")
            String testContent,

            @Schema(description = "합격 기준", example = "60점 이상")
            String passingScore,

            @Schema(description = "카테고리 ID", example = "13")
            Long categoryId,

            @Schema(description = "카테고리명", example = "IT/컴퓨터")
            String categoryName
    ){
        public CertDetailResponse(Certification c) {
            this(
                    c.getId(),
                    c.getName(),
                    c.getIssuer(),
                    c.getOfficialUrl(),
                    c.getTestOverview(),
                    c.getDifficulty(),
                    c.getTestContent(),
                    c.getPassingScore(),
                    c.getCertCategory().getId(),
                    c.getCertCategory().getName()
            );
        }
    }



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

    public record CertListViewResponse(

            @Schema(description = "자격증 ID", example = "1")
            Long certId,
            @Schema(description = "자격증명", example = "정보처리기사")
            String certName,
            @Schema(description = "접수 시작일", example = "2025-07-01")
            LocalDate applyFrom,
            @Schema(description = "시행기관", example = "한국산업인력공단")
            String issuer,
            @Schema(description = "접수 시작일", example = "2025-07-01")
            LocalDate testDateFrom,

            @Schema(description = "시험일", example = "2025-07-01")
            boolean isBookmarked

    ){}

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageDTO<T> {
        private List<T> content;   // 데이터 목록
        private int page;          // 현재 페이지 번호 (0부터 시작)
        private int size;          // 페이지 크기
        private boolean hasNext;   // 다음 페이지 존재 여부
        private Long totalPage;    // 전체 페이지 수
    }


}
