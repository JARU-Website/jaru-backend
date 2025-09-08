package com.web.jaru.certifications.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.jaru.certifications.domain.*;
import com.web.jaru.certifications.dto.CertDTO;
import com.web.jaru.certifications.dto.CertScheduleDTO;
import com.web.jaru.user_scrap_cert.domain.QUserScrapCert;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CertificationCustomRepositoryImpl implements CertificationCustomRepository{

    private final JPAQueryFactory queryFactory;
    QCertification cert = QCertification.certification;
    QCertSchedule schedule = QCertSchedule.certSchedule;
    QUserScrapCert userCert = QUserScrapCert.userScrapCert;

    // ==================== 직무별 top 자격증 조회 - 사용자 제외, 카테고리 선택 메인화면 ====================
    @Override
    public List<CertDTO.PopularCertResponse> viewPopularCertByCategory(Long categoryId) {

        var query = queryFactory.select(Projections.constructor(
                CertDTO.PopularCertResponse.class,
                cert.id,
                cert.name,
                schedule.applyFrom,
                schedule.applyTo
        )).orderBy(cert.applicantsNum.desc()).fetch();
        return query;
    }

    // ==================== 카테고리별 자격증 조회, 여러 카테고리 선택 가능 3개, 페이징, 사용자 포함====================
    @Override
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> viewRecentCertByCategory(
            User user, Long[] categoryList, int page, int size) {

        List<Long> categoryIds = categoryList == null ? List.of() :
                Arrays.stream(categoryList).toList();

        BooleanBuilder where = new BooleanBuilder();
        if (!categoryIds.isEmpty()) {
            where.and(cert.certCategory.id.in(categoryIds));
        }

        // 전체 개수 조회
        long totalCount = queryFactory
                .select(cert.count())
                .from(cert)
                .where(where)
                .fetchOne();

        // 실제 데이터 조회
        List<CertDTO.CertListViewResponse> content = queryFactory
                .select(Projections.constructor(
                        CertDTO.CertListViewResponse.class,
                        cert.id,
                        cert.name,
                        schedule.applyFrom.min(),
                        cert.issuer,
                        schedule.testDateFrom.min(),
                        Expressions.booleanTemplate(
                                "{0} is not null", user == null ? Expressions.nullExpression() : userCert.id
                        )
                ))
                .from(cert)
                .leftJoin(schedule).on(schedule.certification.eq(cert))
                .leftJoin(userCert).on(userCert.certification.eq(cert)
                        .and(user != null ? userCert.user.eq(user) : null))
                .where(where)
                .groupBy(cert.id, cert.name, cert.issuer, userCert.id)
                .orderBy(schedule.applyFrom.desc(), cert.name.desc())
                .offset((long) page * size)   // page는 0부터 시작
                .limit(size)
                .fetch();

        long totalPage = (totalCount + size - 1) / size; // 올림 계산
        boolean hasNext = (page + 1) < totalPage;

        return new CertDTO.PageDTO<>(content, page, size, hasNext, totalPage);
    }


    // ==================== 직무별 top 자격증 조회, 여러 카테고리 선택 가능 3개, 페이징, 사용자 포함===================
    @Override
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> viewTopCertByCategory(
            User user, Long[] categoryList, int page, int size) {

        List<Long> categoryIds = categoryList == null ? List.of() :
                Arrays.stream(categoryList).toList();

        BooleanBuilder where = new BooleanBuilder();
        if (!categoryIds.isEmpty()) {
            where.and(cert.certCategory.id.in(categoryIds));
        }

        // 전체 개수 조회
        long totalCount = queryFactory
                .select(cert.count())
                .from(cert)
                .where(where)
                .fetchOne();

        // 실제 데이터 조회
        List<CertDTO.CertListViewResponse> content = queryFactory
                .select(Projections.constructor(
                        CertDTO.CertListViewResponse.class,
                        cert.id,
                        cert.name,
                        schedule.applyFrom.min(),
                        cert.issuer,
                        schedule.testDateFrom.min(),
                        Expressions.booleanTemplate(
                                "{0} is not null", user == null ? Expressions.nullExpression() : userCert.id
                        )
                ))
                .from(cert)
                .leftJoin(schedule).on(schedule.certification.eq(cert))
                .leftJoin(userCert).on(userCert.certification.eq(cert)
                        .and(user != null ? userCert.user.eq(user) : null))
                .where(where)
                .groupBy(cert.id, cert.name, cert.issuer, userCert.id, cert.applicantsNum)
                .orderBy(cert.applicantsNum.desc(), cert.id.desc())
                .offset((long) page * size)   // page 0부터 시작
                .limit(size)
                .fetch();

        long totalPage = (totalCount + size - 1) / size; // 올림 계산
        boolean hasNext = (page + 1) < totalPage;

        return new CertDTO.PageDTO<>(content, page, size, hasNext, totalPage);
    }

    // ==================== 검색어, 카테고리로 자격증 검색, 로그인된 사용자면 검색기록 저장===================
    @Override
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> searchCerts(
            User user, String keyword, Long[] categoryList, int page, int size) {

        List<Long> categoryIds = categoryList == null ? List.of() :
                Arrays.stream(categoryList).toList();

        BooleanBuilder where = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            where.and(cert.name.containsIgnoreCase(keyword));
        }
        if (!categoryIds.isEmpty()) {
            where.and(cert.certCategory.id.in(categoryIds));
        }

        // 전체 개수
        long totalCount = queryFactory
                .select(cert.count())
                .from(cert)
                .where(where)
                .fetchOne();

        // 실제 데이터 조회
        List<CertDTO.CertListViewResponse> content = queryFactory
                .select(Projections.constructor(
                        CertDTO.CertListViewResponse.class,
                        cert.id,
                        cert.name,
                        schedule.applyFrom.min(),
                        cert.issuer,
                        schedule.testDateFrom.min(),
                        Expressions.booleanTemplate(
                                "{0} is not null", user == null ? Expressions.nullExpression() : userCert.id
                        )
                ))
                .from(cert)
                .leftJoin(schedule).on(schedule.certification.eq(cert))
                .leftJoin(userCert).on(userCert.certification.eq(cert)
                        .and(user != null ? userCert.user.eq(user) : null))
                .where(where)
                .groupBy(cert.id, cert.name, cert.issuer, userCert.id)
                .orderBy(schedule.applyFrom.desc(), cert.name.desc())
                .offset((long) page * size) // page 0부터 시작
                .limit(size)
                .fetch();

        long totalPage = (totalCount + size - 1) / size;
        boolean hasNext = (page + 1) < totalPage;

        return new CertDTO.PageDTO<>(content, page, size, hasNext, totalPage);
    }


    // ==================== 사용자가 북마크한 자격증 조회 ====================
    @Override
    public List<Certification> findScrappedCertifications() {
        return null;
    }
}
