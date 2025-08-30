package com.web.jaru.posts_comments.service;

import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.posts.controller.dto.request.CommentRequest;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.posts_comments.domain.Comment;
import com.web.jaru.posts_comments.repository.CommentRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

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
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void checkDeletedComment(Comment comment) {
        if (!comment.isDeleted()) {
            throw new CustomException(ErrorCode.COMMENT_BAD_REQUEST);
        }
    }
}