package com.web.jaru.post_comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.jaru.post_comment.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


import static com.web.jaru.post_comment.domain.QComment.comment;
import static com.web.jaru.users.domain.QUser.user;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findRootComments(Long postId, Pageable pageable) {
        // 루트댓글 목록 (삭제된 댓글도 조회)
        List<Comment> commentList = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.writer, user).fetchJoin()
                .where(
                        comment.post.id.eq(postId),
                        comment.parent.isNull()
                )
                .orderBy(
                        comment.createdDate.desc(),
                        comment.id.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 루트댓글 수
        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.post.id.eq(postId),
                        comment.parent.isNull()
                )
                .fetchOne();

        return new PageImpl<>(commentList, pageable, total == null ? 0 : total);
    }

    @Override
    public List<Comment> findRepliesByParentIds(List<Long> parentIds) {

        if (parentIds == null || parentIds.isEmpty()) return List.of();

        // 대댓글 목록 반환
        return queryFactory
                .selectFrom(comment)
                .leftJoin(comment.writer, user).fetchJoin()
                .where(
                        comment.parent.isNull(),
                        comment.parent.id.in(parentIds))
                .orderBy(
                        comment.parent.id.asc(),
                        comment.createdDate.asc(),
                        comment.id.asc()
                )
                .fetch();
    }
}
