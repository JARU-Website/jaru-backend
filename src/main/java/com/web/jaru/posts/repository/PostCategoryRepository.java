package com.web.jaru.posts.repository;

import com.web.jaru.posts.domain.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    Optional<PostCategory> findByName(String name);
}
