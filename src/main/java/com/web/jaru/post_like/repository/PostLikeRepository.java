package com.web.jaru.post_like.repository;

import com.web.jaru.post_like.domain.PostLike;
import com.web.jaru.posts.domain.Post;
import com.web.jaru.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
}
