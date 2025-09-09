package com.web.jaru.post_poll.repository;

import com.web.jaru.post_poll.domain.PollOption;
import com.web.jaru.post_poll.domain.PollVote;
import com.web.jaru.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {

    Boolean existsByUserAndOption(User user, PollOption option);

    void deleteByUserAndOption(User user, PollOption option);

    Long countByUserAndOption(User user, PollOption option);
}
