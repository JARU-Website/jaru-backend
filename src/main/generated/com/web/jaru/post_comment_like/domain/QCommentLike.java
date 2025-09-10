package com.web.jaru.post_comment_like.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentLike is a Querydsl query type for CommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentLike extends EntityPathBase<CommentLike> {

    private static final long serialVersionUID = -115185039L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentLike commentLike = new QCommentLike("commentLike");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    public final com.web.jaru.post_comment.domain.QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.web.jaru.users.domain.QUser user;

    public QCommentLike(String variable) {
        this(CommentLike.class, forVariable(variable), INITS);
    }

    public QCommentLike(Path<? extends CommentLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentLike(PathMetadata metadata, PathInits inits) {
        this(CommentLike.class, metadata, inits);
    }

    public QCommentLike(Class<? extends CommentLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new com.web.jaru.post_comment.domain.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.user = inits.isInitialized("user") ? new com.web.jaru.users.domain.QUser(forProperty("user")) : null;
    }

}

