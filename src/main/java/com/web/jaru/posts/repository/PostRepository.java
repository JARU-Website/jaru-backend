package com.web.jaru.posts.repository;

import com.web.jaru.posts.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

}
