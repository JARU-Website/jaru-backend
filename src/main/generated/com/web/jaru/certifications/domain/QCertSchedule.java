package com.web.jaru.certifications.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCertSchedule is a Querydsl query type for CertSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCertSchedule extends EntityPathBase<CertSchedule> {

    private static final long serialVersionUID = 737966749L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCertSchedule certSchedule = new QCertSchedule("certSchedule");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    public final DatePath<java.time.LocalDate> applyFrom = createDate("applyFrom", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> applyTo = createDate("applyTo", java.time.LocalDate.class);

    public final QCertification certification;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final DatePath<java.time.LocalDate> resultDate = createDate("resultDate", java.time.LocalDate.class);

    public final EnumPath<ScheduleType> scheduleType = createEnum("scheduleType", ScheduleType.class);

    public final DatePath<java.time.LocalDate> testDateFrom = createDate("testDateFrom", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> testDateTo = createDate("testDateTo", java.time.LocalDate.class);

    public QCertSchedule(String variable) {
        this(CertSchedule.class, forVariable(variable), INITS);
    }

    public QCertSchedule(Path<? extends CertSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCertSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCertSchedule(PathMetadata metadata, PathInits inits) {
        this(CertSchedule.class, metadata, inits);
    }

    public QCertSchedule(Class<? extends CertSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.certification = inits.isInitialized("certification") ? new QCertification(forProperty("certification"), inits.get("certification")) : null;
    }

}

