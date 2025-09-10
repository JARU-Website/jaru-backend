package com.web.jaru.post_poll.repository;

import com.web.jaru.post_poll.domain.Poll;
import com.web.jaru.post_poll.domain.PollOption;
import com.web.jaru.post_poll.domain.PollVote;
import com.web.jaru.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {

    Boolean existsByUserAndOption(User user, PollOption option);
    Boolean existsByUserAndPoll(User user, Poll poll);

    // 사용자-질문 기준 현재 투표응답
    List<PollVote> findAllByPollAndUser(Poll poll, User user);

    // 집계
    long countByPollId(Long pollId);
    long countByPollIdAndOptionId(Long pollId, Long optionId);

    // 삭제
    void deleteByPollAndUser(Poll poll, User user);
    void deleteByOptionAndUser(PollOption option, User user);

}
