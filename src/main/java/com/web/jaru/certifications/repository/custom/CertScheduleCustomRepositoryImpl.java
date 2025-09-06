package com.web.jaru.certifications.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.jaru.certifications.domain.QCertSchedule;
import com.web.jaru.certifications.domain.QCertification;
import com.web.jaru.certifications.dto.CertScheduleDTO;
import com.web.jaru.user_alarm_cert.domain.QUserAlarmCert;
import com.web.jaru.user_scrap_cert.domain.QUserScrapCert;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
class CertScheduleCustomRepositoryImpl implements CertScheduleCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 월별 내 자격증 조회
    @Override
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyCertSchedule(User user, YearMonth yearMonth, boolean isAlarmed) {
        QCertSchedule cs = QCertSchedule.certSchedule;
        QCertification c = QCertification.certification;
        QUserScrapCert b = QUserScrapCert.userScrapCert;
        QUserAlarmCert a = QUserAlarmCert.userAlarmCert;

        LocalDate start = yearMonth.atDay(1);

        LocalDate end = yearMonth.atEndOfMonth();

        return jpaQueryFactory
                .select(Projections.constructor(
                        CertScheduleDTO.MyCertScheduleResponse.class,
                        c.id,
                        b.id.isNotNull(),
                        a.id.isNotNull(),
                        cs.applyFrom,
                        cs.testDateFrom,
                        cs.resultDate,
                        c.name
                ))
                .from(cs)
                .join(cs.certification, c)
                .leftJoin(b).on(b.certification.eq(c).and(b.user.eq(user)))
                .leftJoin(a).on(a.certification.eq(c).and(a.user.eq(user)))
                .where(
                        cs.applyFrom.between(start, end)
                                .or(cs.testDateFrom.between(start, end))
                                .or(cs.resultDate.between(start, end)),
                        isAlarmed ? a.id.isNotNull() : a.id.isNotNull()
                )
                .fetch();

    }
    // 기간별 자격증 조회(알림설정 true false)
    @Override
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyCertScheduleByDay(User user, LocalDate start, LocalDate end,boolean isAlarmed) {
        QCertSchedule cs = QCertSchedule.certSchedule;
        QCertification c = QCertification.certification;
        QUserScrapCert b = QUserScrapCert.userScrapCert;
        QUserAlarmCert a = QUserAlarmCert.userAlarmCert;

        return jpaQueryFactory
                .select(Projections.constructor(
                        CertScheduleDTO.MyCertScheduleResponse.class,
                        c.id,
                        b.id.isNotNull(),
                        a.id.isNotNull(),
                        cs.applyFrom,
                        cs.testDateFrom,
                        cs.resultDate,
                        c.name
                ))
                .from(cs)
                .join(cs.certification, c)
                .leftJoin(b).on(b.certification.eq(c).and(b.user.eq(user)))
                .leftJoin(a).on(a.certification.eq(c).and(a.user.eq(user)))
                .where(
                        cs.applyFrom.between(start, end)
                                .or(cs.testDateFrom.between(start, end))
                                .or(cs.resultDate.between(start, end))
                        ,
                        isAlarmed ? a.id.isNotNull() : a.id.isNotNull()
                )
                .orderBy(
                        cs.applyFrom.asc(),
                        cs.testDateFrom.asc(),
                        cs.resultDate.asc()
                )
                .fetch();
    }
}