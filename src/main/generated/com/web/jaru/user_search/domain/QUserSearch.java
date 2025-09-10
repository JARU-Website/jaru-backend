package com.web.jaru.user_search.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserSearch is a Querydsl query type for UserSearch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserSearch extends EntityPathBase<UserSearch> {

    private static final long serialVersionUID = 1163120830L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserSearch userSearch = new QUserSearch("userSearch");

    public final com.web.jaru.QBaseTimeEntity _super = new com.web.jaru.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath searchKeyword = createString("searchKeyword");

    public final com.web.jaru.users.domain.QUser user;

    public QUserSearch(String variable) {
        this(UserSearch.class, forVariable(variable), INITS);
    }

    public QUserSearch(Path<? extends UserSearch> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserSearch(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserSearch(PathMetadata metadata, PathInits inits) {
        this(UserSearch.class, metadata, inits);
    }

    public QUserSearch(Class<? extends UserSearch> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.web.jaru.users.domain.QUser(forProperty("user")) : null;
    }

}

