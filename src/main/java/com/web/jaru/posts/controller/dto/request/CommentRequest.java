package com.web.jaru.posts.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CommentRequest {
    private CommentRequest(){}

    public record Create(
            @NotBlank String content,
            @Positive Long parentId
    ) {}

    public record Update(
            @NotBlank String content
    ) {}


}
