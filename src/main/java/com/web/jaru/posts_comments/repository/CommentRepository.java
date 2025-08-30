package com.web.jaru.posts_comments.repository;

import com.web.jaru.posts_comments.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByParentId(Long parentId); // 대댓글 수
}
