package com.web.jaru.certifications.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CertificationCustomRepositoryImpl {

    private final JPAQueryFactory queryFactory;


}
