package com.web.jaru.post_poll.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPollVote is a Querydsl query type for PollVote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPollVote extends EntityPathBase<PollVote> {

    private static final long serialVersionUID = 762988950L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPollVote pollVote = new QPollVote("pollVote");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QPollOption option;

    public final QPoll poll;

    public final com.web.jaru.users.domain.QUser user;

    public QPollVote(String variable) {
        this(PollVote.class, forVariable(variable), INITS);
    }

    public QPollVote(Path<? extends PollVote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPollVote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPollVote(PathMetadata metadata, PathInits inits) {
        this(PollVote.class, metadata, inits);
    }

    public QPollVote(Class<? extends PollVote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.option = inits.isInitialized("option") ? new QPollOption(forProperty("option"), inits.get("option")) : null;
        this.poll = inits.isInitialized("poll") ? new QPoll(forProperty("poll"), inits.get("poll")) : null;
        this.user = inits.isInitialized("user") ? new com.web.jaru.users.domain.QUser(forProperty("user")) : null;
    }

}

