package com.web.jaru.post_poll.repository;

import com.web.jaru.post_poll.domain.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {

}
