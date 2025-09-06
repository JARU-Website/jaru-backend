package com.web.jaru.certifications.repository;

import com.web.jaru.certifications.domain.CertSchedule;
import com.web.jaru.certifications.repository.custom.CertScheduleCustomRepository;
import com.web.jaru.certifications.repository.custom.CertificationCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CertScheduleRepository extends JpaRepository<CertSchedule,Long> , CertScheduleCustomRepository {
}
