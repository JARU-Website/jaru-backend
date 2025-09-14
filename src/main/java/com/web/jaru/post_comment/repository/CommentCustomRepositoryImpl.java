package com.web.jaru.post_comment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.jaru.post_comment.domain.Comment;
import com.web.jaru.post_comment.domain.QComment;
import com.web.jaru.posts.controller.dto.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.web.jaru.post_comment.domain.QComment.comment;
import static com.web.jaru.posts.domain.QPost.post;
import static com.web.jaru.posts.domain.QPostCategory.postCategory;
import static com.web.jaru.users.domain.QUser.user;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

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

    @Override
    public Page<CommentResponse.MyComment> findMyCommentListByCategory(Long userId, Long postCategoryId, Pageable pageable) {
        // static import: comment, post, postCategory
        QComment commentSub = new QComment("commentSub"); // 서브쿼리 전용 별칭

        // where: 작성자 필수 + (선택) 카테고리 필터
        BooleanBuilder where = new BooleanBuilder()
                .and(comment.writer.id.eq(userId));
        if (postCategoryId != null) {
            where.and(post.postCategory.id.eq(postCategoryId));
        }

        // 본문 조회: MyComment DTO 프로젝션
        List<CommentResponse.MyComment> content = queryFactory
                .select(Projections.constructor(
                        CommentResponse.MyComment.class,
                        comment.id,                      // commentId
                        post.id,                         // postId
                        comment.content,                 // content
                        postCategory.name,               // postCategoryName
                        post.title,                      // postTitle
                        JPAExpressions.select(commentSub.id.count().intValue())
                                .from(commentSub)
                                .where(commentSub.post.eq(post)),   // 해당 게시글 전체 댓글 수
                        comment.createdDate              // createdDate
                ))
                .from(comment)
                .join(comment.post, post)
                .leftJoin(post.postCategory, postCategory)
                .where(where)
                .orderBy(
                        comment.createdDate.desc(),     // 최신 댓글 우선
                        comment.id.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수(내가 쓴 댓글 수, 카테고리 필터 동일 적용)
        Long total = queryFactory
                .select(comment.id.count())
                .from(comment)
                .join(comment.post, post)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}
