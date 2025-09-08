package com.web.jaru.post_poll.repository;

import com.web.jaru.post_poll.domain.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
}
