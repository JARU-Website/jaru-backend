package com.web.jaru.posts.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.jaru.posts.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import static com.web.jaru.posts.domain.QPost.post;
import static com.web.jaru.users.domain.QUser.user;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findNewest(Long postCategoryId, Long certCategoryId, Pageable pageable) {
        // 게시글 목록
        List<Post> postList = queryFactory
                .selectFrom(post)
                .where(
                        notDeleted(),
                        eqIfPresent(post.postCategory.id, postCategoryId),
                        eqIfPresent(post.certCategory.id, certCategoryId)
                )
                .orderBy(
                        post.createdDate.desc(), // 1차: 최신순
                        post.id.desc(),
                        post.likeCount.desc() // 2차: 추천순
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카테고리 내 전체 게시글 개수
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        notDeleted(),
                        eqIfPresent(post.postCategory.id, postCategoryId),
                        eqIfPresent(post.certCategory.id, certCategoryId)
                )
                .fetchOne();

        return new PageImpl<>(postList, pageable, total == null ? 0 : total);

    }

    @Override
    public Page<Post> findMostLiked(Long postCategoryId, Long certCategoryId, Pageable pageable) {
        // 게시글 목록
        List<Post> postList = queryFactory
                .selectFrom(post)
                .leftJoin(post.writer, user).fetchJoin()
                .where(
                        notDeleted(),
                        eqIfPresent(post.postCategory.id, postCategoryId),
                        eqIfPresent(post.certCategory.id, certCategoryId)
                )
                .orderBy(
                        post.likeCount.desc(), // 1차: 추천순
                        post.createdDate.desc(), // 2차: 최신순
                        post.id.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카테고리 내 전체 게시글 수
        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        notDeleted(),
                        eqIfPresent(post.postCategory.id, postCategoryId),
                        eqIfPresent(post.certCategory.id, certCategoryId)
                )
                .fetchOne();

        return new PageImpl<>(postList, pageable, total == null ? 0 : total);
    }

    @Override
    public List<Post> findTopMostLiked(int limit) {
        LocalDateTime since = LocalDateTime.now().minusWeeks(3); // 최근 3주

        return queryFactory
                .selectFrom(post)
                .where(
                        notDeleted(),
                        post.createdDate.goe(since)
                )
                .orderBy(
                        post.view.desc(), // 1차: 조회수순
                        post.likeCount.desc(), // 2차: 추천순
                        post.createdDate.desc(), // 2차: 최신순
                        post.id.desc()
                )
                .limit(limit)
                .fetch();
    }

    // 구현 예정
    @Override
    public List<Post> findBySearchWord(String word) {
        return List.of();
    }

    /* ----- 공통 헬퍼 ----- */
    private static BooleanExpression eqIfPresent(NumberPath<Long> path, Long value) {
        return value == null ? null : path.eq(value);
    }

    private BooleanExpression notDeleted() {
        return post.isDeleted.isFalse();
    }

}
