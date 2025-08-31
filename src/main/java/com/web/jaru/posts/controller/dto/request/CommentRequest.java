package com.web.jaru.posts.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CommentRequest {
    private CommentRequest() {}

    public record Create(
            @NotBlank @Size(max = 1000) String content,
            @Positive Long parentId
    ) {}

    public record Update(
            @NotBlank @Size(max = 1000) String content
    ) {}


}
