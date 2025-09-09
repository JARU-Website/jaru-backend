package com.web.jaru.user_alarm_cert.repository;

import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.user_alarm_cert.domain.UserAlarmCert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAlarmRepository extends JpaRepository<UserAlarmCert,Long> {
    void deleteByCertification(Certification certification);
}
