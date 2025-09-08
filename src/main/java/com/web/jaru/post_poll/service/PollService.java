package com.web.jaru.post_poll.service;

import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.post_poll.domain.Poll;
import com.web.jaru.post_poll.domain.PollOption;
import com.web.jaru.post_poll.dto.request.PollRequest;
import com.web.jaru.post_poll.repository.PollOptionRepository;
import com.web.jaru.post_poll.repository.PollRepository;
import com.web.jaru.post_poll.repository.PollVoteRepository;
import com.web.jaru.posts.controller.dto.response.PostResponse;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.domain.PostCategory;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PollService {

    private final PostRepository postRepository;
    private final PollRepository pollRepository;
    private final PollVoteRepository pollVoteRepository;

    // 투표 생성
    @Transactional
    public Long createPoll(Long postId, PollRequest.Create req) {

        Post findPost = getPostOrThrow(postId);

        // 중복 방지
        checkExitsByPostId(postId);

        Poll poll = Poll.builder()
                .title(req.title())
                .post(findPost)
                .build();

        poll.setPost(findPost);

        // 투표 옵션 저장 및 연관관계 설정
        for (String text : req.options()) {
            PollOption pollOption = PollOption.builder()
                    .text(text)
                    .build();
            pollOption.setPoll(poll);
        }

        return pollRepository.save(poll).getId();
    }

    // 투표 조회
    public PostResponse.Poll findPoll(Post post, User loginUser) {

        Poll findPoll = getPollOrThrow(post);

        final int total = findPoll.getTotalVoteCount();

        final Long loginUserId = loginUser != null ? loginUser.getId() : null;

        List<PostResponse.Option> optionList = new ArrayList<>();

        for (PollOption option : findPoll.getOptions()) {

            String percentage = "0.00";

            if (total > 0) {
                percentage = String.format("%.2f", (option.getVoteCount() * 100.0) / total);
            }

            // 로그인유저의 해당 옵션에 투표 여부 확인
            boolean loginUserVoted = false;
            if (loginUserId != null && pollVoteRepository.existsByUserAndOption(loginUser, option)) {
                loginUserVoted = true;
            }

            optionList.add(new PostResponse.Option(
                    option.getId(),
                    option.getText(),
                    option.getVoteCount(),
                    percentage,
                    loginUserVoted
            ));
        }

        return new PostResponse.Poll(
                findPoll.getId(),
                findPoll.getTitle(),
                findPoll.getTotalVoteCount(),
                optionList
        );
    }


    /* --- 예외 처리 --- */

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Poll getPollOrThrow(Post post) {
        return pollRepository.findByPoll(post)
                .orElseThrow(() -> new CustomException(ErrorCode.POLL_NOT_FOUND));
    }

    private void checkExitsByPostId(Long postId) {
        if (pollRepository.existsByPostId(postId)) {
            throw new CustomException(ErrorCode.EXIST_POLL_BY_POST);
        }
    }
}
