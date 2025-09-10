package com.web.jaru.certifications.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCertification is a Querydsl query type for Certification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCertification extends EntityPathBase<Certification> {

    private static final long serialVersionUID = 2053746488L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCertification certification = new QCertification("certification");

    public final NumberPath<Long> applicantsNum = createNumber("applicantsNum", Long.class);

    public final QCertCategory certCategory;

    public final StringPath certType = createString("certType");

    public final NumberPath<Short> difficulty = createNumber("difficulty", Short.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath issuer = createString("issuer");

    public final StringPath name = createString("name");

    public final StringPath officialUrl = createString("officialUrl");

    public final StringPath passingScore = createString("passingScore");

    public final StringPath testContent = createString("testContent");

    public final StringPath testOverview = createString("testOverview");

    public QCertification(String variable) {
        this(Certification.class, forVariable(variable), INITS);
    }

    public QCertification(Path<? extends Certification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCertification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCertification(PathMetadata metadata, PathInits inits) {
        this(Certification.class, metadata, inits);
    }

    public QCertification(Class<? extends Certification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.certCategory = inits.isInitialized("certCategory") ? new QCertCategory(forProperty("certCategory")) : null;
    }

}

