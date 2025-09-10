package com.web.jaru.post_poll.repository;

import com.web.jaru.post_poll.domain.Poll;
import com.web.jaru.post_poll.domain.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {

    List<PollOption> findAllByPoll(Poll poll);
}
