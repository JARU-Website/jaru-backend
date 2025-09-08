package com.web.jaru.posts.service;

import com.web.jaru.certifications.domain.CertCategory;
import com.web.jaru.certifications.repository.CertCategoryRepository;
import com.web.jaru.common.dto.response.PageDto;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.post_like.domain.PostLike;
import com.web.jaru.post_like.repository.PostLikeRepository;
import com.web.jaru.post_poll.dto.request.PollRequest;
import com.web.jaru.post_poll.service.PollService;
import com.web.jaru.posts.controller.dto.request.PostRequest;
import com.web.jaru.posts.controller.dto.response.PostResponse;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.domain.PostCategory;
import com.web.jaru.posts.repository.PostCategoryRepository;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.users.domain.User;
import com.web.jaru.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final CertCategoryRepository certCategoryRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PollService pollService;

    // 게시글 생성
    @Transactional
    public Long createPost(Long loginUserId, PostRequest.Create req) {

        User findUser = getUserOrThrow(loginUserId);
        // 필수 : 게시글 카테고리
        PostCategory findPostCategory = getPostCategoryOrThrow(req.postCategoryId());

        // 선택 : 자격증 카테고리
        CertCategory findCertCategory = null;
        if (req.certCategoryId() != null) {
            findCertCategory = getCertCategoryOrThrow(req.certCategoryId());
        }

        Post post = Post.builder()
                .title(req.title())
                .content(req.content())
                .writer(findUser)
                .postCategory(findPostCategory)
                .certCategory(findCertCategory)
                .build();

        Long savedPostId = postRepository.save(post).getId();

        // 투표 생성
        PollRequest.Create pollReq = req.poll();

        if (pollReq != null) {
            pollService.createPoll(savedPostId, pollReq);
        }

        return savedPostId;
    }

    // 게시글 목록 조회 (최신순)
    public PageDto<PostResponse.Summary> findNewest(Long postCategoryId, Long certCategoryId, Pageable pageable) {

        PostCategory postCategory = (postCategoryId != null) ? getPostCategoryOrThrow(postCategoryId) : null;
        CertCategory  certCategory = (certCategoryId != null) ? getCertCategoryOrThrow(certCategoryId) : null;

        Page<Post> page = postRepository.findNewest(postCategoryId, certCategoryId, pageable);

        Page<PostResponse.Summary> result = page.map(p -> {
            String postCategoryName = (postCategory != null)
                    ? postCategory.getName()
                    : (p.getPostCategory() != null ? p.getPostCategory().getName() : null);

            String certCategoryName = (certCategory != null)
                    ? certCategory.getName()
                    : (p.getCertCategory() != null ? p.getCertCategory().getName() : null);

            return toSummaryDto(p, postCategoryName, certCategoryName);
        });

        return PageDto.of(result);
    }


    // 게시글 목록 조회 (추천순)
    public PageDto<PostResponse.Summary> findMostLiked(Long postCategoryId, Long certCategoryId, Pageable pageable) {

        PostCategory postCategory = (postCategoryId != null) ? getPostCategoryOrThrow(postCategoryId) : null;
        CertCategory  certCategory = (certCategoryId != null) ? getCertCategoryOrThrow(certCategoryId) : null;

        Page<Post> page = postRepository.findMostLiked(postCategoryId, certCategoryId, pageable);

        Page<PostResponse.Summary> result = page.map(p -> {
            String postCategoryName = (postCategory != null)
                    ? postCategory.getName()
                    : (p.getPostCategory() != null ? p.getPostCategory().getName() : null);

            String certCategoryName = (certCategory != null)
                    ? certCategory.getName()
                    : (p.getCertCategory() != null ? p.getCertCategory().getName() : null);

            return toSummaryDto(p, postCategoryName, certCategoryName);
        });

        return PageDto.of(result);
    }

    // 게시글 상세 조회
    public PostResponse.Post findPost(Long postId, User loginUser) {

        Post findPost = getPostOrThrow(postId);

        boolean isLiked = false;

        if (loginUser != null && postLikeRepository.existsByUserAndPost(loginUser, findPost)) {
            isLiked = true;
        }

        PostResponse.Poll poll = pollService.findPoll(findPost, loginUser);

        return toPostDto(findPost, poll, isLiked);
    }

    // 게시글 수정
    @Transactional
    public void editPost(Long postId, Long loginUserId, PostRequest.PatchUpdate req) {

        Post findPost = getPostOrThrow(postId);
        User findUser = getUserOrThrow(loginUserId);

        // 권한 확인
        checkEditPost(findUser, findPost);

        // 최소 1개 이상 변경값 확인
        if (req.title() == null && req.content() == null
                && req.postCategoryId() == null && req.certCategoryId() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        // 들어온 필드만 적용
        if (req.title() != null) {
            findPost.changeTitle(req.title());
        }
        if (req.content() != null) {
            findPost.changeContent(req.content());
        }
        if (req.postCategoryId() != null) {
            PostCategory postCategory = getPostCategoryOrThrow(req.postCategoryId());
            findPost.changePostCategory(postCategory);
        }
        if (req.certCategoryId() != null) {
            CertCategory certCategory = getCertCategoryOrThrow(req.certCategoryId());
            findPost.changeCertCategory(certCategory);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long loginUserId) {

        Post findPost = getPostOrThrow(postId);
        User findUser = getUserOrThrow(loginUserId);

        // 권한 확인
        checkEditPost(findUser, findPost);

        findPost.changeDeletedBy(findUser);
        findPost.softDelete();
    }

    // 게시글 삭제 - 관리자
    @Transactional
    public void deletePostByAdmin(Long postId, User loginUser) {

        Post findPost = getPostOrThrow(postId);

        // 관리자 권한 확인
        checkIsAdmin(loginUser);

        findPost.changeDeletedBy(loginUser);
        findPost.softDelete();
    }

    // 게시글 좋아요
    @Transactional
    public void savePostLike(Long postId, Long loginUserId) {

        Post findPost = getPostOrThrow(postId);
        User findUser = getUserOrThrow(loginUserId);

        // Column unique 제약조건 핸들링 (중복 컬럼 검증)
        if (postLikeRepository.existsByUserAndPost(findUser, findPost)) {
            throw new CustomException(ErrorCode.EXIST_POST_LIKE);
        }

        PostLike postLike = PostLike.builder()
                .user(findUser)
                .post(findPost)
                .build();

        postLikeRepository.save(postLike);

        findPost.plusLikeCount();
    }

    // 게시글 좋아요 취소
    @Transactional
    public void deletePostLike(Long postId, Long loginUserId) {

        User findUser = getUserOrThrow(loginUserId);

        Post findPost = getPostOrThrow(postId);

        postLikeRepository.deleteByUserAndPost(findUser, findPost);

        findPost.minusLikeCount();
    }

    /* --- 예외 처리 --- */
    private User getUserOrThrow(Long loginUserId) {
        return userRepository.findById(loginUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    private PostCategory getPostCategoryOrThrow(Long postCategoryId) {
        return postCategoryRepository.findById(postCategoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_CATEGORY_NOT_FOUND));
    }

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private CertCategory getCertCategoryOrThrow(Long certCategoryId) {
        return certCategoryRepository.findById(certCategoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CERT_CATEGORY_NOT_FOUND));
    }

    private void checkEditPost(User user, Post post) {
        if (!post.getWriter().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }
    }

    private void checkIsAdmin(User user) {
        if (!user.getRole().equals("ADMIN")) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }
    }

    /* --- 엔티티 → 응답 DTO 매핑 --- */
    private PostResponse.Summary toSummaryDto(Post post, String postCategoryName, String certCategoryName) {
        return new PostResponse.Summary(
                post.getId(),
                post.getTitle(),
                postCategoryName,
                certCategoryName,
                post.getCommentCount(),
                post.getLikeCount(),
                post.getView(),
                post.getWriter().getNickname(),
                post.getCreatedDate()
        );
    }

    private PostResponse.Post toPostDto(Post post, PostResponse.Poll poll, boolean isLiked) {
        return new PostResponse.Post(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPostCategory().getName(),
                post.getCertCategory().getName(),
                isLiked,
                post.getLikeCount(),
                post.getView(),
                post.getCommentCount(),
                post.getWriter().getNickname(),
                poll,
                post.getCreatedDate()
        );
    }

}
