package com.web.jaru.certifications.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCertCategory is a Querydsl query type for CertCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCertCategory extends EntityPathBase<CertCategory> {

    private static final long serialVersionUID = 1486398724L;

    public static final QCertCategory certCategory = new QCertCategory("certCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QCertCategory(String variable) {
        super(CertCategory.class, forVariable(variable));
    }

    public QCertCategory(Path<? extends CertCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCertCategory(PathMetadata metadata) {
        super(CertCategory.class, metadata);
    }

}

