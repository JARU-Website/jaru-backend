package com.web.jaru.post_comment.repository;

import com.web.jaru.post_comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    long countByParentId(Long parentId); // 대댓글 수
}
