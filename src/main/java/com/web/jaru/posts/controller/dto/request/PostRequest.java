package com.web.jaru.posts.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public final class PostRequest {

    private PostRequest() {}

    // 게시글 생성
    public record Create(
            @NotBlank @Size(max = 255) String title,
            @NotBlank @Size(max = 3000) String content,
            @NotNull @Positive Long postCategoryId,
            @Positive Long certCategoryId,
            PollCreate poll
    ) { }

    // 게시글 수정
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PatchUpdate(
            @Size(max = 255) String title,
            @Size(max = 3000) String content,
            @Positive Long postCategoryId,
            @Positive Long certCategoryId

    ) { }

    // 투표 생성
    public record PollCreate(
            String title,
            @NotNull @Size(min = 2, max = 10) List<@NotBlank @Size(max = 100) String> options
    ) { }

    // 투표 응답 저장
    public record VoteUpsert(
            @NotNull Long pollId,
            @NotNull @Size(min = 0, max = 3) List<Long> optionIds
    ) { }

}
