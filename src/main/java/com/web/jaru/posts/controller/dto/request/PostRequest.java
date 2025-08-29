package com.web.jaru.posts.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public final class PostRequest {

    private PostRequest() {}

    // 게시글 생성
    public static record Create(
            @NotBlank String title,
            @NotBlank String content,
            @NotBlank @Positive Long postCategoryId,
            @NotBlank @Positive Long certCategoryId
    ) { }
}
