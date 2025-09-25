package com.web.jaru.posts.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public final class CommentResponse {

    private CommentResponse() {}

    // 단일 댓글
    public record Comment(
            Long commentId,
            Long parentId,
            Long postId,
            Long writerId,
            String writerNickname,
            String content,
            int likeCount,
            int replyCount,
            boolean isDeleted,
            boolean isWriter, // 댓글 작성자 본인 여부
            boolean isLiked, // 댓글에 좋아요 표시 여부
            LocalDateTime createdDate
    ) { }

    // 루트 + 대댓글 묶음
    public record CommentThread(
            Comment root,
            List<Comment> replies
    ) { }
}
