package com.web.jaru.security.service;

import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.security.service.CustomUserDetails;
import com.web.jaru.users.domain.Status;
import com.web.jaru.users.domain.User;
import com.web.jaru.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("loadUserByUsername() called with email: {}", email);


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다: {}", email);
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });
        if (user.getDeletedAt() != null || user.getStatus()==Status.INACTIVE) {
            throw new CustomException(ErrorCode.USER_INACTIVE); // 탈퇴한 사용자의 로그인 차단
        }

        return new CustomUserDetails(user);
    }
}