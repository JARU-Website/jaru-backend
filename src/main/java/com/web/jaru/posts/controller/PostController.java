package com.web.jaru.posts.controller;

import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.posts.controller.dto.request.PostRequest;
import com.web.jaru.posts.service.PostService;
import com.web.jaru.users.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createPost (User user, @Valid @RequestBody PostRequest.Create req) {
        Long postId = postService.createPost(user, req);
        return ApiResponse.onSuccess(postId, SuccessCode.CREATED);
    }
}
