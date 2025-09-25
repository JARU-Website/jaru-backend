package com.web.jaru.user_alarm_cert.service;

import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.certifications.repository.CertificationRepository;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.user_alarm_cert.domain.UserAlarmCert;
import com.web.jaru.user_alarm_cert.repository.UserAlarmRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAlramCertService {

    private final CertificationRepository certificationRepository;

    private final UserAlarmRepository userAlarmRepository;

    // 자격증 알림 설정하기
    @Transactional
    public void setCertAlarm(User user, Long certId) {
        Certification certification = certificationRepository.findById(certId)
                .orElseThrow(()->new CustomException(ErrorCode.CERTIFICATION_NOT_FOUND));
        UserAlarmCert userAlarmCert = UserAlarmCert.builder()

                .build();
    }
    // 자격증 알림 취소하기
    @Transactional
    public void cancelCertAlarm(User user, Long certId) {
        Certification certification = certificationRepository.findById(certId)
                .orElseThrow(()->new CustomException(ErrorCode.CERTIFICATION_NOT_FOUND));
        userAlarmRepository.deleteByCertification(certification);
    }
}
