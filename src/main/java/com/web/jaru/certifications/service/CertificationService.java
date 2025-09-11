package com.web.jaru.certifications.service;

import com.web.jaru.certifications.domain.CertCategory;
import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.certifications.dto.CertDTO;
import com.web.jaru.certifications.dto.CertScheduleDTO;
import com.web.jaru.certifications.repository.CertCategoryRepository;
import com.web.jaru.certifications.repository.CertScheduleRepository;
import com.web.jaru.certifications.repository.CertificationRepository;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.user_scrap_cert.domain.UserScrapCert;
import com.web.jaru.user_scrap_cert.repository.UserScrapCertRepository;
import com.web.jaru.user_search.domain.UserSearch;
import com.web.jaru.user_search.repository.UserSearchRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserScrapCertRepository userScrapCertRepository;
    private final CertScheduleRepository certScheduleRepository;
    private final UserSearchRepository userSearchRepository;
    private final CertCategoryRepository certCategoryRepository;

    /* ---  MY 자격증 조회 --- */
    // 월별 내 자격증 조회
    @Transactional(readOnly = true)
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyMonthlyCert(User user, YearMonth yearMonth, boolean isAlarmed){
        return certScheduleRepository.viewMyCertSchedule(user, yearMonth,isAlarmed);
    }

    // 선택한 기간 내의 자격증 조회하기
    @Transactional(readOnly = true)
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyCertScheduleByDay(User user, LocalDate start, LocalDate end,boolean isAlarmed){
        return certScheduleRepository.viewMyCertScheduleByDay(user,start,end,isAlarmed);
    }

    /* ---  자격증 상세 조회 --- */
    // id로 자격증 조회
    @Transactional(readOnly = true)
    public CertDTO.CertDetailResponse viewCertDetail(Long certificationId) {
        Certification findCertification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CERTIFICATION_NOT_FOUND));
        return new CertDTO.CertDetailResponse(findCertification);
    }
    // 검색어, 카테고리로 자격증 검색, 로그인된 사용자면 검색기록 저장
    @Transactional(readOnly = true)
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> searchCertByCategory(
            User user, String keyword, Long[] categoryList, int page, int size){
        if(user!=null) {
            UserSearch userSearch = UserSearch.builder()
                    .searchKeyword(keyword)
                    .user(user)
                    .build();
            userSearchRepository.save(userSearch);
        }
        return certificationRepository.searchCerts(user, keyword, categoryList, page, size);
    }

    // 검색 기록 조회
    @Transactional(readOnly = true)
    public List<String> getRecentSearchKeywords(User user) {
            return userSearchRepository.findTop8ByUserOrderByCreatedDateDesc(user)
                    .stream()
                    .map(UserSearch::getSearchKeyword)
                    .toList();
    }

    /* ---  월별 자격증 일정 조회 --- */
    // 월별 + 카테고리별 조회 및 검색
    @Transactional(readOnly = true)
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMonthlyCertByUser(User user, YearMonth yearMonth, Long categoryId, String searchKeyword) {
        return certScheduleRepository.viewCertSchedule(user, yearMonth, categoryId, searchKeyword);

    }
    // 선택한 기간 내의 자격증 조회
    @Transactional(readOnly = true)
    public List<CertScheduleDTO.MyCertScheduleResponse> viewCertScheduleByDay(User user, LocalDate start, LocalDate end, Long categoryId, String searchKeyword){
        return certScheduleRepository.viewCertScheduleByDay(user, start, end, categoryId, searchKeyword);
    }

    /* ---  자격증 리스트 조회 --- */
    // 카테고리별로 조회, 여러 카테고리 선택 가능
    @Transactional(readOnly = true)
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> getRecentCertByCategory(
            User user, Long[] categoryList, int page, int size) {
        if (categoryList != null && categoryList.length > 4) {
            throw new CustomException(ErrorCode.CATEGORY_SELECTION_LIMIT_EXCEEDED);
        }
        return certificationRepository.viewRecentCertByCategory(user, categoryList, page, size);
    }
    // 직무별 top 자격증 조회, 여러 카테고리 선택 가능
    @Transactional(readOnly = true)
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> getTopCertByCategory(
            User user, Long[] categoryList, int page, int size) {
        if (categoryList != null && categoryList.length > 4) {
            throw new CustomException(ErrorCode.CATEGORY_SELECTION_LIMIT_EXCEEDED);
        }
        return certificationRepository.viewTopCertByCategory(user, categoryList, page, size);
    }

    /* --- 회원의 자격증 스크랩 --- */
    @Transactional
    public void saveScrapInfo(User user, Long certificationId) {

        Certification findCertification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CERTIFICATION_NOT_FOUND,
                        "자격증을 찾을 수 없습니다. id=" + certificationId));

        // 이미 스크랩한 정보일 경우
        if (userScrapCertRepository.existsByUserAndCertification(user, findCertification)) {
            throw new CustomException(ErrorCode.EXIST_USER_SCRAP_CERT);
        }

        UserScrapCert userScrapCert = UserScrapCert.builder()
                .user(user)
                .certification(findCertification)
                .build();

        userScrapCertRepository.save(userScrapCert);
    }

    @Transactional
    public void deleteScrapInfo(User user, Long certificationId) {

        Certification findCertification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CERTIFICATION_NOT_FOUND));

        // 이미 스크랩한 정보일 경우
        if (!userScrapCertRepository.existsByUserAndCertification(user, findCertification)) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER_SCRAP_CERT);
        }

        userScrapCertRepository.deleteByUserAndCertification(user, findCertification);
    }

    public CertDTO.PageDTO<CertDTO.CertListViewResponse> findScrapList(List<Long> certCategoryIds, User user, int page, int size) {

        if (certCategoryIds != null && !certCategoryIds.isEmpty()) return findScrapListWithCategory(certCategoryIds, user, page, size);
        return userScrapCertRepository.findScrapCertList(user.getId(), page, size);
    }

    public CertDTO.PageDTO<CertDTO.CertListViewResponse> findScrapListWithCategory(List<Long> certCategoryIds, User user, int page, int size) {

        if (certCategoryIds != null && certCategoryIds.size() > 4) {
            throw new CustomException(ErrorCode.CATEGORY_SELECTION_LIMIT_EXCEEDED);
        }

        return userScrapCertRepository.findScrapCertListWithCategory(certCategoryIds, user.getId(), page, size);
    }
}
