package com.web.jaru.posts.controller;

import com.web.jaru.common.dto.response.PageDto;
import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.posts.controller.dto.request.CommentRequest;
import com.web.jaru.posts.controller.dto.request.PostRequest;
import com.web.jaru.posts.controller.dto.response.CommentResponse;
import com.web.jaru.posts.controller.dto.response.PostResponse;
import com.web.jaru.posts.service.PostService;
import com.web.jaru.post_comment.service.CommentService;
import com.web.jaru.security.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ApiResponse<Long> createPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @Valid @RequestBody PostRequest.Create req) {

        Long postId = postService.createPost(userDetails.getUser().getId(), req);

        return ApiResponse.onSuccess(postId, SuccessCode.POST_SAVED);
    }

    // 게시글 목록 조회 (최신순)
    @GetMapping("/list/newest")
    public ApiResponse<PageDto<PostResponse.Summary>> findNewestPostList(@RequestParam(name = "postCategoryId") Long postCategoryId, @RequestParam(name = "certCategoryId", required = false) Long certCategoryId,
                                                                         @PageableDefault(page = 0, size = 10)  Pageable pageable) {

        return ApiResponse.onSuccess(postService.findNewest(postCategoryId, certCategoryId, pageable), SuccessCode.OK);
    }

    // 게시글 목록 조회 (추천순)
    @GetMapping("/list/most-liked")
    public ApiResponse<PageDto<PostResponse.Summary>> findMostLikedPostList(@RequestParam(name = "postCategoryId") Long postCategoryId, @RequestParam(name = "certCategoryId", required = false) Long certCategoryId,
                                                                            @PageableDefault(page = 0, size = 10)  Pageable pageable) {

        return ApiResponse.onSuccess(postService.findMostLiked(postCategoryId, certCategoryId, pageable), SuccessCode.OK);
    }

    // 게시글 상세 조회 (회원)
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse.Post> getPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @PathVariable(value = "postId") Long postId) {

        return ApiResponse.onSuccess(postService.findPost(postId, userDetails.getUser().getId()), SuccessCode.OK);
    }

    // 게시글 상세 조회 (비회원)
    public ApiResponse<PostResponse.Post> getPostByNonUser(@PathVariable(value = "postId") Long postId) {

        return ApiResponse.onSuccess(postService.findPostByNonUser(postId), SuccessCode.OK);
    }

    // 게시글 수정
    @PatchMapping("/edit/{postId}")
    public ApiResponse<Void> updatePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @Valid @RequestBody PostRequest.PatchUpdate req,
                                         @PathVariable(value = "postId") Long postId) {

        postService.editPost(postId, userDetails.getUser().getId(), req);

        return ApiResponse.onSuccess(null, SuccessCode.POST_SAVED);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable(value = "postId") Long postId) {

        postService.deletePost(postId, userDetails.getUser().getId());

        return ApiResponse.onSuccess(null, SuccessCode.POST_DELETED);
    }

    /* --- 게시글 좋아요 API --- */

    // 게시글 좋아요 저장
    @PostMapping("/like/{postId}")
    public ApiResponse<Void> savePostLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable(value = "postId") Long postId) {

        postService.savePostLike(postId, userDetails.getUser().getId());

        return ApiResponse.onSuccess(null, SuccessCode.POST_LIKE_SAVED);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/like/{postId}")
    public ApiResponse<Void> deletePostLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable(value = "postId") Long postId) {

        postService.deletePostLike(postId, userDetails.getUser().getId());

        return ApiResponse.onSuccess(null, SuccessCode.POST_LIKE_DELETED);
    }

    /* --- 댓글 API --- */

    // 댓글 생성
    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @Valid @RequestBody CommentRequest.Create req,
                                    @PathVariable(name = "postId") Long postId) {

        Long commentId = commentService.createComment(postId, userDetails.getUser().getId(), req);

        return ApiResponse.onSuccess(commentId, SuccessCode.CREATED);
    }

    // 댓글 목록 조회
    @GetMapping("/{postId}/comments")
    public ApiResponse<PageDto<CommentResponse.CommentThread>> getCommentList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                              @PathVariable(name = "postId") Long postId,
                                                                              @PageableDefault(page = 0, size = 10)  Pageable pageable) {

        return ApiResponse.onSuccess(commentService.findCommentList(postId, userDetails.getUser().getId(), pageable), SuccessCode.OK);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ApiResponse<Void> updateComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable(name = "commentId") Long commentId,
                                           @Valid @RequestBody CommentRequest.Update req) {

        commentService.updateComment(commentId, userDetails.getUser().getId(), req);

        return ApiResponse.onSuccess(null, SuccessCode.COMMENT_SAVED);
    }

    // 댓글 삭제(소프트 삭제)
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable(name = "commentId") Long commentId) {
        commentService.deleteComment(commentId, userDetails.getUser().getId());

        return ApiResponse.onSuccess(null, SuccessCode.COMMENT_DELETED);
    }

    /* --- 댓글 좋아요 API --- */
    // 게시글 좋아요 저장
    @PostMapping("/like/{postId}")
    public ApiResponse<Void> saveCommentLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable(value = "postId") Long postId) {

        commentService.saveCommentLike(postId, userDetails.getUser().getId());

        return ApiResponse.onSuccess(null, SuccessCode.COMMENT_LIKE_SAVED);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/like/{postId}")
    public ApiResponse<Void> deleteCommentLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(value = "postId") Long postId) {

        commentService.deleteCommentLike(postId, userDetails.getUser().getId());

        return ApiResponse.onSuccess(null, SuccessCode.COMMENT_LIKE_DELETED);
    }

}
