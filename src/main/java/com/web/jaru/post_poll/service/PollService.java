package com.web.jaru.post_poll.service;

import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.post_poll.domain.Poll;
import com.web.jaru.post_poll.domain.PollOption;
import com.web.jaru.post_poll.domain.PollVote;
import com.web.jaru.post_poll.repository.PollOptionRepository;
import com.web.jaru.post_poll.repository.PollRepository;
import com.web.jaru.post_poll.repository.PollVoteRepository;
import com.web.jaru.posts.controller.dto.request.PostRequest;
import com.web.jaru.posts.controller.dto.response.PostResponse;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.posts.repository.PostRepository;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PollService {

    private final PostRepository postRepository;
    private final PollRepository pollRepository;
    private final PollVoteRepository pollVoteRepository;
    private final PollOptionRepository pollOptionRepository;

    // 투표 생성
    @Transactional
    public Long createPoll(Long postId, PostRequest.PollCreate req) {

        Post findPost = getPostOrThrow(postId);

        // 중복 방지
        checkExitsByPostId(postId);

        Poll poll = Poll.builder()
                .title(req.title())
                .post(findPost)
                .allowMultiple(req.allowMultiple())
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

        Poll findPoll = getPollByPostOrThrow(post);

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
                findPoll.isAllowMultiple(),
                findPoll.getTotalVoteCount(),
                optionList
        );
    }

    // 투표 응답 갱신
    @Transactional
    public PostResponse.Poll upsertVote(PostRequest.VoteUpsert req, User loginUser) {

        Poll findPoll = getPollOrThrow(req.pollId());

        List<Long> reqOptionIds = req.optionIds();
        if (reqOptionIds == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        // 요청 정제
        List<Long> optionIds = reqOptionIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (findPoll.isAllowMultiple()) {
            throw new CustomException(ErrorCode.POLL_MAX_SELECTION_EXCEEDED);
        }
        else if (optionIds.size() > 3) {
            throw new CustomException(ErrorCode.POLL_MAX_SELECTION_EXCEEDED);
        }

        // 설문 소속 검증
        List<PollOption> pollOptions = pollOptionRepository.findAllByPoll(findPoll);
        Map<Long, PollOption> optionMap = pollOptions.stream()
                .collect(Collectors.toMap(PollOption::getId, Function.identity()));

        for (Long optionId : optionIds) {
            if (!optionMap.containsKey(optionId)) {
                throw new CustomException(ErrorCode.INVALID_POLL_OPTION);
            }
        }

        // 기존 투표 응답 조회 후 카운트 차감
        List<PollVote> prevVotes = pollVoteRepository.findAllByPollAndUser(findPoll, loginUser);
        for (PollVote prev : prevVotes) {
            PollOption prevOpt = prev.getOption();
            prevOpt.minusVoteCount();
            findPoll.minusTotalVoteCount();
        }

        // 기존 투표 응답 삭제
        if (!prevVotes.isEmpty()) {
            pollVoteRepository.deleteByPollAndUser(findPoll, loginUser);
        }

        // 투표 응답 저장
        List<PollVote> savedPollVotes = new ArrayList<>();

        for (int i = 0; i < optionIds.size(); i++) {
            Long optId = optionIds.get(i);
            PollOption opt = optionMap.get(optId);
            opt.plusVoteCount();
            findPoll.plusTotalVoteCount();

            PollVote pv = PollVote.builder()
                    .poll(findPoll)
                    .option(opt)
                    .user(loginUser)
                    .build();
            savedPollVotes.add(pv);
        }

        if (!savedPollVotes.isEmpty()) {
            pollVoteRepository.saveAll(savedPollVotes);
        }

        // ======== 응답 구성 =========
        Set<Long> selectedOptionIds = new HashSet<>(optionIds);

        List<PostResponse.Option> optionResponseList = new ArrayList<>();
        for (PollOption option: pollOptions) {
            String percentage = formatPercent(option.getVoteCount(), findPoll.getTotalVoteCount());
            boolean selected = selectedOptionIds.contains(option.getId());

            optionResponseList.add(new PostResponse.Option(
                    option.getId(),
                    option.getText(),
                    option.getVoteCount(),
                    percentage,
                    selected
            ));
        }

        return new PostResponse.Poll(
                findPoll.getId(),
                findPoll.getTitle(),
                findPoll.isAllowMultiple(),
                findPoll.getTotalVoteCount(),
                optionResponseList
        );
    }

    // 투표 응답 추가
    @Transactional
    public void editPoll(PostRequest.PollEdit req, User loginUser) {

        // 응답 추가
        Poll findPoll = getPollOrThrow(req.pollId());

        // 권한 확인
        checkEditPoll(loginUser, findPoll);

        if (req.title() != null) {
            findPoll.changeTitle(req.title());
        }

        // 투표 옵션 저장 및 연관관계 설정
        for (String text : req.options()) {
            PollOption pollOption = PollOption.builder()
                    .text(text)
                    .build();
            pollOption.setPoll(findPoll);
        }
    }

    private String formatPercent(int count, int total) {
        if (total == 0) return "0.00";
        double p = (count * 100.0) / total;
        return String.format(java.util.Locale.US, "%.2f", p);
    }


    /* --- 예외 처리 --- */

    private Post getPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Poll getPollOrThrow(Long pollId) {
        return pollRepository.findById(pollId)
                .orElseThrow(() -> new CustomException(ErrorCode.POLL_NOT_FOUND));
    }

    private Poll getPollByPostOrThrow(Post post) {
        return pollRepository.findByPost(post)
                .orElseThrow(() -> new CustomException(ErrorCode.POLL_NOT_FOUND));
    }

    private void checkExitsByPostId(Long postId) {
        if (pollRepository.existsByPostId(postId)) {
            throw new CustomException(ErrorCode.EXIST_POLL_BY_POST);
        }
    }

    private void checkEditPoll(User user, Poll poll) {
        if (!poll.getPost().getWriter().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }
    }
}
