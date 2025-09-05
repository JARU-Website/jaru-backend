package com.web.jaru.post_poll.repository;

import com.web.jaru.post_poll.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {

    boolean existsByPostId(Long postId); // 중복생성 방지
}
