package com.web.jaru.user_scrap_cert.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserScrapCert is a Querydsl query type for UserScrapCert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserScrapCert extends EntityPathBase<UserScrapCert> {

    private static final long serialVersionUID = 78042197L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserScrapCert userScrapCert = new QUserScrapCert("userScrapCert");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    public final com.web.jaru.certifications.domain.QCertification certification;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.web.jaru.users.domain.QUser user;

    public QUserScrapCert(String variable) {
        this(UserScrapCert.class, forVariable(variable), INITS);
    }

    public QUserScrapCert(Path<? extends UserScrapCert> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserScrapCert(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserScrapCert(PathMetadata metadata, PathInits inits) {
        this(UserScrapCert.class, metadata, inits);
    }

    public QUserScrapCert(Class<? extends UserScrapCert> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.certification = inits.isInitialized("certification") ? new com.web.jaru.certifications.domain.QCertification(forProperty("certification"), inits.get("certification")) : null;
        this.user = inits.isInitialized("user") ? new com.web.jaru.users.domain.QUser(forProperty("user")) : null;
    }

}

