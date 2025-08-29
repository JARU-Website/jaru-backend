package com.web.jaru.posts.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public final class PostRequest {

    private PostRequest() {}

    // 게시글 생성
    public static record Create(
            @NotBlank @Size(max = 255) String title,
            @NotBlank @Size(max = 3000) String content,
            @NotBlank @Positive Long postCategoryId,
            @Positive Long certCategoryId
    ) { }
}
