package com.web.jaru.posts.service;

import com.web.jaru.certifications.domain.CertCategory;
import com.web.jaru.certifications.repository.CertCategoryRepository;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.posts.controller.dto.request.PostRequest;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.domain.PostCategory;
import com.web.jaru.posts.repository.PostCategoryRepository;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final CertCategoryRepository certCategoryRepository;

    @Transactional
    public Long createPost(User loginUser, PostRequest.Create req) {

        // 필수 : 게시글 카테고리
        PostCategory findPostCategory = postCategoryRepository.findById(req.postCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_CATEGORY_NOT_FOUND));

        // 선택 : 자격증 카테고리
        CertCategory findCertCategory = null;
        if (req.certCategoryId() != null) {
            findCertCategory = certCategoryRepository.findById(req.certCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CERT_CATEGORY_NOT_FOUND));
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
}
