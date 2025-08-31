package com.web.jaru.users.controller;


import com.web.jaru.common.annotation.CurrentUser;
import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.security.service.CustomUserDetails;
import com.web.jaru.users.service.UserService;
import com.web.jaru.users.domain.User;
import com.web.jaru.users.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "쿠키 기반 JWT 인증을 통해 현재 로그인한 회원 정보를 조회합니다.")
    public ApiResponse<UserDTO.UserResponseDTO> getMyInfo(@CurrentUser CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ApiResponse.onSuccess(userService.getUserInfo(user), SuccessCode.OK);
    }


}
