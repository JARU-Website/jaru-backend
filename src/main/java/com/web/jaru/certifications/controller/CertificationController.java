package com.web.jaru.certifications.controller;

import com.web.jaru.certifications.dto.CertDTO;
import com.web.jaru.certifications.dto.CertScheduleDTO;
import com.web.jaru.certifications.service.CertificationService;
import com.web.jaru.common.annotation.CurrentUser;
import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.security.service.CustomUserDetails;
import com.web.jaru.users.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationService certificationService;


    // ================= MY 자격증 조회 ==================
    @GetMapping("/my/monthly")
    @Operation(summary = "월별 내 자격증 조회", description = "해당 월의 내 자격증을 조회합니다. 알림 설정 여부 선택 가능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "UNAUTHORIZED_USER - 인증되지 않은 사용자")
    })
    public ApiResponse<List<CertScheduleDTO.MyCertScheduleResponse>> viewMyMonthlyCert(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "조회 연도", example = "2025") @RequestParam int year,
            @Parameter(description = "조회 월 (1~12)", example = "9") @RequestParam int month,
            @Parameter(description = "알림 설정 여부", example = "true") @RequestParam(defaultValue = "false") boolean isAlarmed) {
        return ApiResponse.onSuccess(
                certificationService.viewMyMonthlyCert(user, YearMonth.of(year, month), isAlarmed),
                SuccessCode.OK
        );
    }

    @GetMapping("/my/range")
    @Operation(summary = "기간 내 내 자격증 조회", description = "선택한 기간 내 내 자격증을 조회합니다. 알림 여부 선택 가능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "CERTIFICATION_NOT_FOUND - 자격증을 찾을 수 없음")
    })
    public ApiResponse<List<CertScheduleDTO.MyCertScheduleResponse>> viewMyCertScheduleByDay(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "조회 시작일", example = "2025-09-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @Parameter(description = "조회 종료일", example = "2025-09-30") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @Parameter(description = "알림 설정 여부", example = "false") @RequestParam(defaultValue = "false") boolean isAlarmed) {
        return ApiResponse.onSuccess(
                certificationService.viewMyCertScheduleByDay(user, start, end, isAlarmed),
                SuccessCode.OK
        );
    }

    // ================= 자격증 상세 조회 ==================
    @GetMapping("/{certificationId}")
    @Operation(summary = "자격증 상세 조회", description = "자격증 ID로 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "CERTIFICATION_NOT_FOUND - 자격증을 찾을 수 없음")
    })
    public ApiResponse<CertDTO.CertDetailResponse> viewCertDetail(
            @Parameter(description = "자격증 ID", required = true, example = "1")
            @PathVariable Long certificationId) {
        return ApiResponse.onSuccess(
                certificationService.viewCertDetail(certificationId),
                SuccessCode.OK
        );
    }

    // ================= 자격증 검색 ==================
    @GetMapping("/search")
    @Operation(summary = "자격증 검색", description = "검색어와 카테고리로 자격증을 검색합니다. 로그인된 사용자는 검색 기록이 저장됩니다.")
    public ApiResponse<CertDTO.PageDTO<CertDTO.CertListViewResponse>> searchCertByCategory(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "검색 키워드", example = "정보처리") @RequestParam(required = false) String keyword,
            @Parameter(description = "카테고리 ID 배열", example = "[1,2,3]") @RequestParam(required = false) Long[] categoryIds,
            @Parameter(description = "페이지 번호(0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(
                certificationService.searchCertByCategory(
                        user != null ? user : null,
                        keyword,
                        categoryIds,
                        page,
                        size
                ),
                SuccessCode.OK
        );
    }

    @GetMapping("/search/history")
    @Operation(summary = "검색 기록 조회", description = "사용자의 최근 검색어 8개를 최신순으로 조회합니다.")
    public ApiResponse<List<String>> getRecentSearchKeywords(
            @Parameter(hidden = true) @CurrentUser CustomUserDetails user) {
        return ApiResponse.onSuccess(
                certificationService.getRecentSearchKeywords(user.getUser()),
                SuccessCode.OK
        );
    }

    // ================= 월별 자격증 일정 조회 ==================
    @GetMapping("/monthly")
    @Operation(summary = "월별 자격증 일정 조회", description = "월별 + 카테고리별 자격증 일정을 조회합니다.")
    public ApiResponse<List<CertScheduleDTO.MyCertScheduleResponse>> viewMonthlyCertByUser(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "조회 연도", example = "2025") @RequestParam int year,
            @Parameter(description = "조회 월", example = "9") @RequestParam int month,
            @Parameter(description = "카테고리 ID", example = "13") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "검색 키워드", example = "SQLD") @RequestParam(required = false) String searchKeyword) {
        return ApiResponse.onSuccess(
                certificationService.viewMonthlyCertByUser(user, YearMonth.of(year, month), categoryId, searchKeyword),
                SuccessCode.OK
        );
    }

    @GetMapping("/range")
    @Operation(summary = "기간 내 자격증 일정 조회", description = "기간 내 + 카테고리별 자격증 일정을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "UNAUTHORIZED_USER - 인증되지 않은 사용자")
    })
    public ApiResponse<List<CertScheduleDTO.MyCertScheduleResponse>> viewCertScheduleByDay(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "조회 시작일", example = "2025-09-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @Parameter(description = "조회 종료일", example = "2025-09-30") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @Parameter(description = "카테고리 ID", example = "13") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "검색 키워드", example = "SQLD") @RequestParam(required = false) String searchKeyword) {
        return ApiResponse.onSuccess(
                certificationService.viewCertScheduleByDay(user, start, end, categoryId, searchKeyword),
                SuccessCode.OK
        );
    }

    // ================= 자격증 리스트 조회 ==================
    @GetMapping("/recent")
    @Operation(summary = "카테고리별 자격증 조회", description = "카테고리별 자격증을 최신순으로 조회합니다. 여러 카테고리 선택 가능 (최대 4개)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "CATEGORY_SELECTION_LIMIT_EXCEEDED - 카테고리를 너무 많이 선택")
    })
    public ApiResponse<CertDTO.PageDTO<CertDTO.CertListViewResponse>> getRecentCertByCategory(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "카테고리 ID 배열", example = "[1,2,3]") @RequestParam(required = false) Long[] categoryIds,
            @Parameter(description = "페이지 번호(0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(
                certificationService.getRecentCertByCategory(
                        user != null ? user : null,
                        categoryIds,
                        page,
                        size
                ),
                SuccessCode.OK
        );
    }

    @GetMapping("/top")
    @Operation(summary = "직무별 Top 자격증 조회", description = "지원자 수(applicantsNum) 기준으로 직무별 Top 자격증을 조회합니다. 여러 카테고리 선택 가능 (최대 4개)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "CATEGORY_SELECTION_LIMIT_EXCEEDED - 카테고리를 너무 많이 선택")
    })
    public ApiResponse<CertDTO.PageDTO<CertDTO.CertListViewResponse>> getTopCertByCategory(
            @Parameter(hidden = true) @CurrentUser User user,
            @Parameter(description = "카테고리 ID 배열", example = "[1,2,3]") @RequestParam(required = false) Long[] categoryIds,
            @Parameter(description = "페이지 번호(0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(
                certificationService.getTopCertByCategory(
                        user != null ? user : null,
                        categoryIds,
                        page,
                        size
                ),
                SuccessCode.OK
        );
    }


    // ======== 회원의 자격증 스크랩 =======

    // 자격증 스크랩 저장
    @PostMapping("/scrap/{certificationId}")
    public ApiResponse<Void> saveScrapInfo(@CurrentUser User user,
                                           @PathVariable(value = "certificationId") Long certificationId) {
        certificationService.saveScrapInfo(user, certificationId);
        return ApiResponse.onSuccess(null, SuccessCode.CERT_SCRAP_SAVED);

    }

    // 자격증 스크랩 삭제
    @DeleteMapping("/scrap/{certificationId}")
    public ApiResponse<Void> deleteScrapInfo(@CurrentUser User user,
                                             @PathVariable(value = "certificationId") Long certificationId) {
        certificationService.deleteScrapInfo(user, certificationId);
        return ApiResponse.onSuccess(null, SuccessCode.CERT_SCRAP_DELETED);
    }

}
