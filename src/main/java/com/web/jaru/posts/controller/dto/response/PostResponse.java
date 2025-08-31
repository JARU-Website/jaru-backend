package com.web.jaru.posts.controller.dto.response;

import java.time.LocalDateTime;

public final class PostResponse {
    private PostResponse() {}

    // 게시글 목록
    public record Summary(
            Long postId,
            String title,
            String postCategoryName,
            String CertCategoryName,
            int commentCount,
            int likeCount,
            int view,
            String writerNickname, // 닉네임
            LocalDateTime createdDate
    ) { }

    // 게시글 상세
    public record Post(
            Long postId,
            String title,
            String content,
            String postCategoryName,
            String certCategoryName,
            int likeCount,
            int view,
            int commentCount,
            String writerNickname

    ) { }
}
