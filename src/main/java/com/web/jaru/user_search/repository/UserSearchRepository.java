package com.web.jaru.user_search.repository;

import com.web.jaru.user_search.domain.UserSearch;
import com.web.jaru.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserSearchRepository extends JpaRepository<UserSearch,Long> {
    List<UserSearch> findTop8ByUserOrderByCreatedDateDesc(User user);
}
