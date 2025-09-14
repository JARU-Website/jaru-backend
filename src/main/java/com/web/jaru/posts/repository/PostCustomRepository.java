package com.web.jaru.posts.repository;

import com.web.jaru.posts.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostCustomRepository {

    Page<Post> findNewest(Long postCategoryId, Long certCategoryId, Pageable pageable);

    Page<Post> findMostLiked(Long postCategoryId, Long certCategoryId, Pageable pageable);

    List<Post> findTopMostLiked(int limit);

    List<Post> findBySearchWord(String word);

    Page<Post> findMyNewest(Long userId, Long postCategoryId, Long certCategoryId, Pageable pageable);
}
