package com.web.jaru.post_comment.service;

import com.web.jaru.common.dto.response.PageDto;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.post_comment_like.domain.CommentLike;
import com.web.jaru.post_comment_like.repository.CommentLikeRepository;
import com.web.jaru.post_like.domain.PostLike;
import com.web.jaru.posts.controller.dto.request.CommentRequest;
import com.web.jaru.posts.controller.dto.response.CommentResponse;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.post_comment.domain.Comment;
import com.web.jaru.post_comment.repository.CommentRepository;
import com.web.jaru.users.domain.User;
import com.web.jaru.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 생성
    @Transactional
    public Long createComment(Long postId, User loginUser, CommentRequest.Create req) {

        Post findPost = getPostOrThrow(postId);

        Comment parent = null;

        // 대댓글
        if (req.parentId() != null) {
            parent = getCommentOrThrow(req.parentId());
            // 부모는 루트이고, 같은 게시글이어야 함
            if (!parent.isRoot() || !parent.getPost().getId().equals(postId)) {
                throw new CustomException(ErrorCode.COMMENT_BAD_REQUEST);
            }
            parent.plusReplyCount();
        }

        Comment comment = Comment.builder()
                .post(findPost)
                .writer(loginUser)
                .parent(parent)
                .content(req.content())
                .build();

        return commentRepository.save(comment).getId();
    }

    // 댓글 목록 조회 (대댓글 포함)
    public PageDto<CommentResponse.CommentThread> findCommentList(Long postId, User loginUser, Pageable pageable) {

        // 1. 루트 댓글 페이지
        Page<Comment> rootList = commentRepository.findRootComments(postId, pageable);

        // 2. 대댓글 일괄 조회
        List<Long> rootIds = rootList.getContent().stream()
                .map(Comment::getId)
                .toList();

        List<Comment> replyList = commentRepository.findRepliesByParentIds(rootIds);

        // 3. 루트 댓글 - 대댓글 매핑 (루트 순서 유지)
        Map<Long, List<Comment>> replyMap = new LinkedHashMap<>();

        for (Long rootId : rootIds) replyMap.put(rootId, new ArrayList<>());

        for (Comment reply : replyList) {
            Long parentId = reply.getParent().getId();
            replyMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(reply);
        }

        // 4. 엔티티 -> DTO 변환
        List<CommentResponse.CommentThread> resultList = rootList.getContent().stream()
                .map(root -> new CommentResponse.CommentThread(
                        toDto(root, loginUser),
                        replyMap.getOrDefault(root.getId(), List.of()).stream()
                                .map(comment -> toDto(comment, loginUser))
                                .toList()
                ))
                .toList();

        Page<CommentResponse.CommentThread> page = new PageImpl<>(resultList, pageable, rootList.getTotalElements());

        return PageDto.of(page);
    }

    // 내 댓글 목록 조회
    public PageDto<CommentResponse.MyComment> findMyCommentList(User loginUser, Long postCategoryId, Pageable pageable) {

        Page<CommentResponse.MyComment> page = commentRepository.findMyCommentListByCategory(loginUser.getId(), postCategoryId, pageable);

        return PageDto.of(page);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, User loginUser, CommentRequest.Update req) {

        Comment findComment = getCommentOrThrow(commentId);

        // 권한 확인
        checkEditComment(loginUser, findComment);
        // 삭제 여부
        checkDeletedComment(findComment);

        findComment.changeContent(req.content());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, User loginUser) {

        Comment findComment = getCommentOrThrow(commentId);

        // 권한 확인
        checkEditComment(loginUser, findComment);
        // 삭제 여부
        checkDeletedComment(findComment);

        findComment.softDelete();

        if (!findComment.isRoot()) {
            findComment.getParent().minusReplyCount();
        }

    }

    // 댓글 좋아요
    @Transactional
    public void saveCommentLike(Long commentId, User loginUser) {

        Comment findComment = getCommentOrThrow(commentId);

        // Column unique 제약조건 핸들링 (중복 컬럼 검증)
        if (commentLikeRepository.existsByUserAndComment(loginUser, findComment)) {
            throw new CustomException(ErrorCode.EXIST_POST_LIKE);
        }

        CommentLike commentLike = CommentLike.builder()
                .user(loginUser)
                .comment(findComment)
                .build();

        commentLikeRepository.save(commentLike);

        findComment.plusLikeCount();
    }

    // 댓글 좋아요 취소
    @Transactional
    public void deleteCommentLike(Long commentId, User loginUser) {

        Comment findComment = getCommentOrThrow(commentId);

        commentLikeRepository.deleteByUserAndComment(loginUser, findComment);

        findComment.minusLikeCount();
    }

    /* --- 예외 처리 --- */

    private Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void checkEditComment(User user, Comment comment) {
        if (!comment.getWriter().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }
    }

    private void checkDeletedComment(Comment comment) {
        if (comment.isDeleted()) {
            throw new CustomException(ErrorCode.COMMENT_BAD_REQUEST);
        }
    }

    /* --- 엔티티 → 응답 DTO 매핑 --- */
    private CommentResponse.Comment toDto(Comment c, User loginUser) {
        return new CommentResponse.Comment(
                c.getId(),
                c.getParent() == null ? null : c.getParent().getId(),
                c.getPost().getId(),
                c.getWriter().getId(),
                c.getWriter().getNickname(),
                c.isDeleted() ? "" : c.getContent(),
                c.getLikeCount(),
                c.isRoot() ? c.getReplyCount() : 0,   // 루트에만 의미
                c.isDeleted(),
                loginUser != null && c.getWriter().equals(loginUser),
                loginUser != null && commentLikeRepository.existsByUserAndComment(loginUser, c),
                c.getCreatedDate()
        );
    }
}