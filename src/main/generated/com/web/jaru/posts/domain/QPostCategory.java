package com.web.jaru.posts.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostCategory is a Querydsl query type for PostCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostCategory extends EntityPathBase<PostCategory> {

    private static final long serialVersionUID = -2080865696L;

    public static final QPostCategory postCategory = new QPostCategory("postCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QPostCategory(String variable) {
        super(PostCategory.class, forVariable(variable));
    }

    public QPostCategory(Path<? extends PostCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostCategory(PathMetadata metadata) {
        super(PostCategory.class, metadata);
    }

}

