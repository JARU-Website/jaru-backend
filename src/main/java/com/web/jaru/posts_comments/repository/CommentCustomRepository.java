package com.web.jaru.posts_comments.repository;

import com.web.jaru.posts_comments.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {

    Page<Comment> findRootComments(Long postId, Pageable pageable);
    List<Comment> findRepliesByParentIds(List<Long> parentIds);
}
