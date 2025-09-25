package com.web.jaru.certifications.repository;

import com.web.jaru.certifications.domain.CertSchedule;
import com.web.jaru.certifications.repository.custom.CertScheduleCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertScheduleRepository extends JpaRepository<CertSchedule,Long> , CertScheduleCustomRepository {
}
