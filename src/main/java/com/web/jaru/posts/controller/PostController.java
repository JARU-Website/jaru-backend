package com.web.jaru.posts.controller;

import com.web.jaru.common.dto.response.PageDto;
import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.posts.controller.dto.request.CommentRequest;
import com.web.jaru.posts.controller.dto.request.PostRequest;
import com.web.jaru.posts.controller.dto.response.CommentResponse;
import com.web.jaru.posts.controller.dto.response.PostResponse;
import com.web.jaru.posts.service.PostService;
import com.web.jaru.posts_comments.service.CommentService;
import com.web.jaru.users.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    // 게시글 생성
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createPost(User user,
                                         @Valid @RequestBody PostRequest.Create req) {
        Long postId = postService.createPost(user, req);
        return ApiResponse.onSuccess(postId, SuccessCode.CREATED);
    }

    // 게시글 목록 조회 (최신순)
    @GetMapping("/list/{postCategoryId}/{certCategoryId}")
    public ApiResponse<PageDto<PostResponse.Summary>> findNewestPostList(@PathVariable("postCategoryId") Long postCategoryId, @PathVariable(name = "certCategoryId", required = false) Long certCategoryId,
                                                                         @PageableDefault(page = 0, size = 10)  Pageable pageable) {
        return ApiResponse.onSuccess(postService.findNewest(postCategoryId, certCategoryId, pageable), SuccessCode.OK);
    }

    // 게시글 목록 조회 (추천순)
    @GetMapping("/list/{postCategoryId}/{certCategoryId}")
    public ApiResponse<PageDto<PostResponse.Summary>> findMostLikedPostList(@PathVariable("postCategoryId") Long postCategoryId, @PathVariable(name = "certCategoryId", required = false) Long certCategoryId,
                                                                            @PageableDefault(page = 0, size = 10)  Pageable pageable) {
        return ApiResponse.onSuccess(postService.findNewest(postCategoryId, certCategoryId, pageable), SuccessCode.OK);
    }

    // 게시글 상세 조회

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

    /* --- 댓글 API --- */

    // 댓글 생성
    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createComment(User user,
                                    @Valid @RequestBody CommentRequest.Create req,
                                    @PathVariable(name = "postId") Long postId) {

        Long commentId = commentService.createComment(postId, user, req);
        return ApiResponse.onSuccess(commentId, SuccessCode.CREATED);
    }

    // 댓글 목록 조회
    @GetMapping("/{postId}/comments")
    public ApiResponse<PageDto<CommentResponse.CommentThread>> getCommentList(User user,
                                                                              @PathVariable(name = "postId") Long postId,
                                                                              @PageableDefault(page = 0, size = 10)  Pageable pageable) {
        return ApiResponse.onSuccess(commentService.findCommentList(postId, user, pageable), SuccessCode.OK);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ApiResponse<Void> updateComment(User user,
                                           @PathVariable(name = "commentId") Long commentId,
                                           @Valid @RequestBody CommentRequest.Update req) {
        commentService.updateComment(commentId, user, req);
        return ApiResponse.onSuccess(null, SuccessCode.OK);
    }

    // 댓글 삭제(소프트 삭제)
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(User user,
                                           @PathVariable(name = "commentId") Long commentId) {
        commentService.deleteComment(commentId, user);
        return ApiResponse.onSuccess(null, SuccessCode.OK);
    }

}
