package com.web.jaru.posts.controller.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class PostResponse {
    private PostResponse() {}

    // 게시글 목록
    public record Summary(
            Long postId,
            String title,
            String postCategoryName,
            String certCategoryName,
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
            boolean isLiked,
            int likeCount,
            int view,
            int commentCount,
            String writerNickname,
            Poll poll,
            LocalDateTime createdDate
    ) { }

    // 게시글 투표
    public record Poll(
            Long pollId,
            String title,
            int totalVoteCount,
            List<Option> options
    ) { }

    // 게시글 투표 옵션
    public record Option(
            Long optionId,
            String text,
            int voteCount,
            String percentage,
            boolean loginUserSelected
    ) { }
}
