package com.web.jaru.certifications.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.jaru.certifications.domain.QCertCategory;
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

@RequiredArgsConstructor
class CertScheduleCustomRepositoryImpl implements CertScheduleCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // ==================== 월별 내 자격증 조회 ====================
    @Override
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyCertSchedule(User user, YearMonth yearMonth, boolean isAlarmed) {
        QCertSchedule cs = QCertSchedule.certSchedule;
        QCertification c = QCertification.certification;
        QUserScrapCert b = QUserScrapCert.userScrapCert;
        QUserAlarmCert a = QUserAlarmCert.userAlarmCert;

        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        var query = jpaQueryFactory
                .select(Projections.constructor(
                        CertScheduleDTO.MyCertScheduleResponse.class,
                        c.id,
                        user != null ? b.id.isNotNull() : Expressions.constant(false),
                        user != null ? a.id.isNotNull() : Expressions.constant(false),
                        cs.applyFrom,
                        cs.testDateFrom,
                        cs.resultDate,
                        c.name
                ))
                .from(cs)
                .join(cs.certification, c);

        if (user != null) {
            query.leftJoin(b).on(b.certification.eq(c).and(b.user.eq(user)));
            query.leftJoin(a).on(a.certification.eq(c).and(a.user.eq(user)));
        }

        query.where(
                cs.applyFrom.between(start, end)
                        .or(cs.testDateFrom.between(start, end))
                        .or(cs.resultDate.between(start, end)),
                isAlarmed ? a.id.isNotNull() : null
        );

        return query.fetch();
    }

    // ==================== 기간별 내 자격증 조회 ====================
    @Override
    public List<CertScheduleDTO.MyCertScheduleResponse> viewMyCertScheduleByDay(User user, LocalDate start, LocalDate end, boolean isAlarmed) {
        QCertSchedule cs = QCertSchedule.certSchedule;
        QCertification c = QCertification.certification;
        QUserScrapCert b = QUserScrapCert.userScrapCert;
        QUserAlarmCert a = QUserAlarmCert.userAlarmCert;

        var query = jpaQueryFactory
                .select(Projections.constructor(
                        CertScheduleDTO.MyCertScheduleResponse.class,
                        c.id,
                        user != null ? b.id.isNotNull() : Expressions.constant(false),
                        user != null ? a.id.isNotNull() : Expressions.constant(false),
                        cs.applyFrom,
                        cs.testDateFrom,
                        cs.resultDate,
                        c.name
                ))
                .from(cs)
                .join(cs.certification, c);

        if (user != null) {
            query.leftJoin(b).on(b.certification.eq(c).and(b.user.eq(user)));
            query.leftJoin(a).on(a.certification.eq(c).and(a.user.eq(user)));
        }

        query.where(
                cs.applyFrom.between(start, end)
                        .or(cs.testDateFrom.between(start, end))
                        .or(cs.resultDate.between(start, end)),
                isAlarmed ? a.id.isNotNull() : null
        );

        query.orderBy(
                cs.applyFrom.asc(),
                cs.testDateFrom.asc(),
                cs.resultDate.asc()
        );

        return query.fetch();
    }

    // ==================== 기간별, 카테고리/검색어 자격증 조회 ====================
    @Override
    public List<CertScheduleDTO.MyCertScheduleResponse> viewCertScheduleByDay(User user, LocalDate start, LocalDate end, Long categoryId, String searchKeyword) {
        QCertSchedule cs = QCertSchedule.certSchedule;
        QCertification c = QCertification.certification;
        QUserScrapCert b = QUserScrapCert.userScrapCert;
        QUserAlarmCert a = QUserAlarmCert.userAlarmCert;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(
                cs.applyFrom.between(start, end)
                        .or(cs.testDateFrom.between(start, end))
                        .or(cs.resultDate.between(start, end))
        );

        if (categoryId != null) {
            builder.and(c.certCategory.id.eq(categoryId));
        }

        if (searchKeyword != null && !searchKeyword.isBlank()) {
            builder.and(c.name.containsIgnoreCase(searchKeyword));
        }

        var query = jpaQueryFactory
                .select(Projections.constructor(
                        CertScheduleDTO.MyCertScheduleResponse.class,
                        c.id,
                        user != null ? b.id.isNotNull() : Expressions.constant(false),
                        user != null ? a.id.isNotNull() : Expressions.constant(false),
                        cs.applyFrom,
                        cs.testDateFrom,
                        cs.resultDate,
                        c.name
                ))
                .from(cs)
                .join(cs.certification, c);

        if (user != null) {
            query.leftJoin(b).on(b.certification.eq(c).and(b.user.eq(user)));
            query.leftJoin(a).on(a.certification.eq(c).and(a.user.eq(user)));
        }

        query.where(builder);
        query.orderBy(
                cs.applyFrom.asc(),
                cs.testDateFrom.asc(),
                cs.resultDate.asc()
        );

        return query.fetch();
    }

    // ==================== 월별, 카테고리/검색어 자격증 조회 ====================
    @Override
    public List<CertScheduleDTO.MyCertScheduleResponse> viewCertSchedule(User user, YearMonth yearMonth, Long categoryId, String searchKeyword) {
        QCertSchedule cs = QCertSchedule.certSchedule;
        QCertification c = QCertification.certification;
        QUserScrapCert b = QUserScrapCert.userScrapCert;
        QUserAlarmCert a = QUserAlarmCert.userAlarmCert;

        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(
                cs.applyFrom.between(start, end)
                        .or(cs.testDateFrom.between(start, end))
                        .or(cs.resultDate.between(start, end))
        );

        if (categoryId != null) {
            builder.and(c.certCategory.id.eq(categoryId));
        }

        if (searchKeyword != null && !searchKeyword.isBlank()) {
            builder.and(c.name.containsIgnoreCase(searchKeyword));
        }

        var query = jpaQueryFactory
                .select(Projections.constructor(
                        CertScheduleDTO.MyCertScheduleResponse.class,
                        c.id,
                        user != null ? b.id.isNotNull() : Expressions.constant(false),
                        user != null ? a.id.isNotNull() : Expressions.constant(false),
                        cs.applyFrom,
                        cs.testDateFrom,
                        cs.resultDate,
                        c.name
                ))
                .from(cs)
                .join(cs.certification, c);

        if (user != null) {
            query.leftJoin(b).on(b.certification.eq(c).and(b.user.eq(user)));
            query.leftJoin(a).on(a.certification.eq(c).and(a.user.eq(user)));
        }

        query.where(builder);
        query.orderBy(
                cs.applyFrom.asc(),
                cs.testDateFrom.asc(),
                cs.resultDate.asc()
        );

        return query.fetch();
    }

    // ==================== 월별, 카테고리 자격증 조회  - 사용자 제외, 메인화면 ====================
    @Override
    public List<CertScheduleDTO.MonthlyCertResponse> viewMonthlyCertByCategory(
            Long categoryId, User user, YearMonth yearMonth) {
        QCertSchedule cs = QCertSchedule.certSchedule;
        QCertification c = QCertification.certification;
        QCertCategory cc = QCertCategory.certCategory;

        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(
                cs.applyFrom.between(start, end)
                        .or(cs.testDateFrom.between(start, end))
                        .or(cs.resultDate.between(start, end))
        );

        // 카테고리 있으면 조회, 없으면 전체 조회
        if (categoryId != null) {
            builder.and(c.certCategory.id.eq(categoryId));
        }

        var query = jpaQueryFactory.select(Projections.constructor(
                        CertScheduleDTO.MonthlyCertResponse.class,
                        c.id,
                        c.name,
                        cc.id,
                        cc.name,
                        cs.applyFrom,
                        cs.applyTo
                )).from(cs)
                .join(cs.certification, c)
                .join(c.certCategory, cc)
                .where(builder)
                .orderBy(cs.applyFrom.asc())
                .fetch();
        return query;
    }

}
