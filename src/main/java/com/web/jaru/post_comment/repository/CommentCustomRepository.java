package com.web.jaru.post_comment.repository;

import com.web.jaru.post_comment.domain.Comment;
import com.web.jaru.posts.controller.dto.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentCustomRepository {

    Page<Comment> findRootComments(Long postId, Pageable pageable);
    List<Comment> findRepliesByParentIds(List<Long> parentIds);
    Page<CommentResponse.MyComment> findMyCommentListByCategory(Long userId, Long postCategoryId, Pageable pageable);
}
