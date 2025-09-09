package com.web.jaru.post_poll.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPoll is a Querydsl query type for Poll
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPoll extends EntityPathBase<Poll> {

    private static final long serialVersionUID = 2117118092L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPoll poll = new QPoll("poll");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    public final BooleanPath allowMultiple = createBoolean("allowMultiple");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<PollOption, QPollOption> options = this.<PollOption, QPollOption>createList("options", PollOption.class, QPollOption.class, PathInits.DIRECT2);

    public final com.web.jaru.posts.domain.QPost post;

    public final StringPath title = createString("title");

    public final NumberPath<Integer> totalVoteCount = createNumber("totalVoteCount", Integer.class);

    public QPoll(String variable) {
        this(Poll.class, forVariable(variable), INITS);
    }

    public QPoll(Path<? extends Poll> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPoll(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPoll(PathMetadata metadata, PathInits inits) {
        this(Poll.class, metadata, inits);
    }

    public QPoll(Class<? extends Poll> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new com.web.jaru.posts.domain.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

