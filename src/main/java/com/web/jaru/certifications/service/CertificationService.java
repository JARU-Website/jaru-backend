package com.web.jaru.certifications.service;

import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.certifications.repository.CertificationRepository;
import com.web.jaru.certifications.repository.custom.CertificationCustomRepositoryImpl;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.user_scrap_cert.domain.UserScrapCert;
import com.web.jaru.user_scrap_cert.repository.UserScrapCertRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    // 자격증 스크랩
    private final UserScrapCertRepository userScrapCertRepository;

    // ================= MY 자격증 조회 ==================
    // 월별 내 자격증 조회
    @Transactional

    // 선택한 기간 내의 자격증 조회하기
    // 자격증 알림 설정하기
    // 자격증 알림 취소하기
    // 월별 알림설정한 일정만 보기 
    // 구글 캘린더 연동(로그인 시 구글 토큰 db에 저장하는걸로 추가하기? or redis에 추가하기)
    // 구글 캘린더 연동 취소

    // ================= 자격증 상세 조회 ==================
    // id로 자격증 조회

    // ================= 월별 자격증 조회 ==================
    // 월별 + 카테고리별 조회 및 검색
    // 선택한 기간 내의 자격증 조회
    // 선택한 월 카테고리별 조회
    // 선택한 월 전체 카테고리 조회

    // ================= 자격증 리스트 조회 ==================


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
                .orElseThrow(() -> new CustomException(ErrorCode.CERTIFICATION_NOT_FOUND,
                        "자격증을 찾을 수 없습니다. id=" + certificationId));

        // 이미 스크랩한 정보일 경우
        if (!userScrapCertRepository.existsByUserAndCertification(user, findCertification)) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER_SCRAP_CERT);
        }

        userScrapCertRepository.deleteByUserAndCertification(user, findCertification);
    }
}
