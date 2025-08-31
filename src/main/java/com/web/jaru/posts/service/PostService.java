package com.web.jaru.posts.service;

import com.web.jaru.certifications.domain.CertCategory;
import com.web.jaru.certifications.repository.CertCategoryRepository;
import com.web.jaru.common.dto.response.PageDto;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.posts.controller.dto.request.PostRequest;
import com.web.jaru.posts.controller.dto.response.PostResponse;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.domain.PostCategory;
import com.web.jaru.posts.repository.PostCategoryRepository;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.users.domain.User;
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

    // 게시글 생성
    @Transactional
    public Long createPost(User loginUser, PostRequest.Create req) {

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
                .writer(loginUser)
                .postCategory(findPostCategory)
                .certCategory(findCertCategory)
                .build();

        return postRepository.save(post).getId();
    }

    // 게시글 목록 조회 (최신순)
    @Transactional(readOnly = true)
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

            return toSummary(p, postCategoryName, certCategoryName);
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

            return toSummary(p, postCategoryName, certCategoryName);
        });

        return PageDto.of(result);
    }

    // 게시글 수정
    @Transactional
    public void editPost(Long postId, User loginUser, PostRequest.PatchUpdate req) {

        Post post = getPostOrThrow(postId);

        // 권한 확인
        checkEditPost(loginUser, post);

        // 최소 1개 이상 변경값 확인
        if (req.title() == null && req.content() == null
                && req.postCategoryId() == null && req.certCategoryId() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        // 들어온 필드만 적용
        if (req.title() != null) {
            post.changeTitle(req.title());
        }
        if (req.content() != null) {
            post.changeContent(req.content());
        }
        if (req.postCategoryId() != null) {
            PostCategory postCategory = getPostCategoryOrThrow(req.postCategoryId());
            post.changePostCategory(postCategory);
        }
        if (req.certCategoryId() != null) {
            CertCategory certCategory = getCertCategoryOrThrow(req.certCategoryId());
            post.changeCertCategory(certCategory);
        }
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, User loginUser) {

        Post post = getPostOrThrow(postId);

        // 권한 확인
        checkEditPost(loginUser, post);

        post.changeDeletedBy(loginUser);
        post.softDelete();
    }

    // 게시글 삭제 - 관리자
    @Transactional
    public void deletePostByAdmin(Long postId, User loginUser) {

        Post post = getPostOrThrow(postId);

        // 관리자 권한 확인
        checkIsAdmin(loginUser);

        post.changeDeletedBy(loginUser);
        post.softDelete();
    }

    /* --- 예외 처리 --- */
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
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private void checkIsAdmin(User user) {
        if (!user.getRole().equals("ADMIN")) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    /* --- 엔티티 → 응답 DTO 매핑 --- */
    private PostResponse.Summary toSummary(Post post, String postCategoryName, String certCategoryName) {
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

}
