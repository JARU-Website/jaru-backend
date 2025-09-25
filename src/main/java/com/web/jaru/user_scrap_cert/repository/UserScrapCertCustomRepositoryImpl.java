package com.web.jaru.user_scrap_cert.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.certifications.dto.CertDTO;
import com.web.jaru.posts.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.web.jaru.certifications.domain.QCertSchedule.certSchedule;
import static com.web.jaru.certifications.domain.QCertification.certification;
import static com.web.jaru.posts.domain.QPost.post;
import static com.web.jaru.user_scrap_cert.domain.QUserScrapCert.userScrapCert;
import static com.web.jaru.users.domain.QUser.user;

@RequiredArgsConstructor
public class UserScrapCertCustomRepositoryImpl implements UserScrapCertCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> findScrapCertList(Long userId, int page, int size) {

        // 유저 조건만 (카테고리 조건 없음)
        BooleanExpression userFilter = userScrapCert.user.id.eq(userId);

        // 총 개수
        Long totalCount = queryFactory
                .select(userScrapCert.id.count())
                .from(userScrapCert)
                .where(userFilter)
                .fetchOne();
        long total = totalCount == null ? 0L : totalCount;

        // 스케줄 최소값 서브쿼리
        var minApplyFrom = JPAExpressions
                .select(certSchedule.applyFrom.min())
                .from(certSchedule)
                .where(certSchedule.certification.eq(certification));

        var minTestDateFrom = JPAExpressions
                .select(certSchedule.testDateFrom.min())
                .from(certSchedule)
                .where(certSchedule.certification.eq(certification));

        // 콘텐츠: DTO 프로젝션
        List<CertDTO.CertListViewResponse> content = queryFactory
                .select(Projections.constructor(
                        CertDTO.CertListViewResponse.class,
                        certification.id,          // certId
                        certification.name,        // name
                        minApplyFrom,              // earliest applyFrom
                        certification.issuer,      // issuer
                        minTestDateFrom,           // earliest testDateFrom
                        Expressions.constant(true) // scrapped: 스크랩 목록이므로 true
                ))
                .from(userScrapCert)
                .join(userScrapCert.certification, certification)
                .where(userFilter)
                .orderBy(userScrapCert.id.desc())   // 최근 스크랩 우선
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long totalPage = (total + size - 1) / size;
        boolean hasNext = (page + 1) < totalPage;

        return new CertDTO.PageDTO<>(content, page, size, hasNext, totalPage);
    }

    @Override
    public CertDTO.PageDTO<CertDTO.CertListViewResponse> findScrapCertListWithCategory(
            List<Long> certCategoryIds, Long userId, int page, int size) {

        // where 절 (가독성 ↑)
        BooleanBuilder where = new BooleanBuilder()
                .and(userScrapCert.user.id.eq(userId))
                .and(inCategories(certCategoryIds));

        // 총 개수
        Long totalCount = queryFactory
                .select(userScrapCert.id.count())
                .from(userScrapCert)
                .join(userScrapCert.certification, certification)
                .where(where)
                .fetchOne();
        long total = totalCount == null ? 0L : totalCount;

        // 스케줄 최소값 서브쿼리(그룹바이 제거 포인트)
        var minApplyFrom = JPAExpressions
                .select(certSchedule.applyFrom.min())
                .from(certSchedule)
                .where(certSchedule.certification.eq(certification));

        var minTestDateFrom = JPAExpressions
                .select(certSchedule.testDateFrom.min())
                .from(certSchedule)
                .where(certSchedule.certification.eq(certification));

        // 콘텐츠: DTO로 바로 매핑 (가독성 ↑)
        List<CertDTO.CertListViewResponse> content = queryFactory
                .select(Projections.constructor(
                        CertDTO.CertListViewResponse.class,
                        certification.id,            // certId
                        certification.name,          // name
                        minApplyFrom,                // earliest applyFrom
                        certification.issuer,        // issuer
                        minTestDateFrom,             // earliest testDateFrom
                        Expressions.constant(true)   // scrapped: 스크랩 목록이므로 항상 true
                ))
                .from(userScrapCert)
                .join(userScrapCert.certification, certification)
                .where(where)
                .orderBy(userScrapCert.id.desc())   // 최근 스크랩 우선
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long totalPage = (total + size - 1) / size;
        boolean hasNext = (page + 1) < totalPage;

        return new CertDTO.PageDTO<>(content, page, size, hasNext, totalPage);
    }

    private BooleanExpression inCategories(List<Long> ids) {
        return (ids == null || ids.isEmpty())
                ? null
                : certification.certCategory.id.in(ids);
    }
}
