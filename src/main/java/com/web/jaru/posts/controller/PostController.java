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
    public ApiResponse<Long> createPost(User user,
                                         @Valid @RequestBody PostRequest.Create req) {
        Long postId = postService.createPost(user, req);
        return ApiResponse.onSuccess(postId, SuccessCode.CREATED);
    }

    // 게시글 수정
    @PatchMapping("/edit/{postId}")
    public ApiResponse<Void> updatePost(User user,
                                         @Valid @RequestBody PostRequest.PatchUpdate req,
                                         @PathVariable("postId") Long postId) {
        postService.editPost(postId, user, req);

        return ApiResponse.onSuccess(null, SuccessCode.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(User user,
                                        @PathVariable("postId") Long postId) {
        postService.deletePost(postId, user);

        return ApiResponse.onSuccess(null, SuccessCode.OK);
    }
}
