package com.web.jaru.user_alarm_cert.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAlarmCert is a Querydsl query type for UserAlarmCert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAlarmCert extends EntityPathBase<UserAlarmCert> {

    private static final long serialVersionUID = -106702507L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAlarmCert userAlarmCert = new QUserAlarmCert("userAlarmCert");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    public final com.web.jaru.certifications.domain.QCertification certification;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.web.jaru.users.domain.QUser user;

    public QUserAlarmCert(String variable) {
        this(UserAlarmCert.class, forVariable(variable), INITS);
    }

    public QUserAlarmCert(Path<? extends UserAlarmCert> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAlarmCert(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAlarmCert(PathMetadata metadata, PathInits inits) {
        this(UserAlarmCert.class, metadata, inits);
    }

    public QUserAlarmCert(Class<? extends UserAlarmCert> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.certification = inits.isInitialized("certification") ? new com.web.jaru.certifications.domain.QCertification(forProperty("certification"), inits.get("certification")) : null;
        this.user = inits.isInitialized("user") ? new com.web.jaru.users.domain.QUser(forProperty("user")) : null;
    }

}

