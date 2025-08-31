package com.web.jaru.users.service;

import com.web.jaru.users.domain.User;
import com.web.jaru.users.dto.UserDTO;
import com.web.jaru.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 현재 로그인한 유저의 정보 조회
     */
    public UserDTO.UserResponseDTO getUserInfo(User user) {
        return UserDTO.UserResponseDTO.from(user);
    }
}
