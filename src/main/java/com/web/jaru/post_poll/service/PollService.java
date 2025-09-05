package com.web.jaru.post_poll.service;

import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.post_poll.domain.Poll;
import com.web.jaru.post_poll.domain.PollOption;
import com.web.jaru.post_poll.dto.request.PollRequest;
import com.web.jaru.post_poll.repository.PollRepository;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.domain.PostCategory;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PollService {

    private final PostRepository postRepository;
    private final PollRepository pollRepository;

    // 투표 생성
    @Transactional
    public Long createPoll(Long postId, PollRequest.Create req) {

        Post post = getPostOrThrow(postId);

        // 중복 방지
        checkExitsByPostId(postId);

        Poll poll = Poll.builder()
                .post(post)
                .allowMultiple(req.allowMultiple())
                .build();

        // 투표 옵션 저장 및 연관관계 설정
        req.options().forEach(text -> poll.addOption(
                PollOption
                        .builder()
                        .text(text)
                        .build()
        ));

        return pollRepository.save(poll).getId();
    }


    /* --- 예외 처리 --- */

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void checkExitsByPostId(Long postId) {
        if (pollRepository.existsByPostId(postId)) {
            throw new CustomException(ErrorCode.EXIST_POLL_BY_POST);
        }
    }
}
