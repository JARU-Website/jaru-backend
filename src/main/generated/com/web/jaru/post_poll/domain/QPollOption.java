package com.web.jaru.post_poll.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPollOption is a Querydsl query type for PollOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPollOption extends EntityPathBase<PollOption> {

    private static final long serialVersionUID = -1406499807L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPollOption pollOption = new QPollOption("pollOption");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QPoll poll;

    public final StringPath text = createString("text");

    public final NumberPath<Integer> voteCount = createNumber("voteCount", Integer.class);

    public QPollOption(String variable) {
        this(PollOption.class, forVariable(variable), INITS);
    }

    public QPollOption(Path<? extends PollOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPollOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPollOption(PathMetadata metadata, PathInits inits) {
        this(PollOption.class, metadata, inits);
    }

    public QPollOption(Class<? extends PollOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.poll = inits.isInitialized("poll") ? new QPoll(forProperty("poll"), inits.get("poll")) : null;
    }

}

