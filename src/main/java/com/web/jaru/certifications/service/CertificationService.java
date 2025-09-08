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
