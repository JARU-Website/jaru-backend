package com.web.jaru.post_poll.repository;

import com.web.jaru.post_poll.domain.Poll;
import com.web.jaru.posts.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {

    boolean existsByPostId(Long postId); // 중복생성 방지

    Optional<Poll> findByPost(Post post);
}
