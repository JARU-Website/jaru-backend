package com.web.jaru.post_comment_like.repository;

import com.web.jaru.post_comment.domain.Comment;
import com.web.jaru.post_comment_like.domain.CommentLike;
import com.web.jaru.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByUserAndComment(User user, Comment comment);
    void deleteByUserAndComment(User user, Comment comment);
}
