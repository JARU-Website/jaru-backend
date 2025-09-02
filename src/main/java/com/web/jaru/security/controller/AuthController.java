package com.web.jaru.security.controller;

import com.web.jaru.common.annotation.CurrentUser;
import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.security.oauth.OAuthService;
import com.web.jaru.security.service.CustomUserDetails;
import com.web.jaru.users.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "소셜 로그인 및 인증 관련 API")
public class AuthController {

    private final OAuthService authService;

    /**
     * 구글 로그인
     */
    @PostMapping("/google/login")
    @Operation(summary = "구글 로그인", description = "구글 인가 코드를 받아 로그인/회원가입을 처리합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "INVALID_OAUTH_TOKEN - 유효하지 않는 구글 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "UNAUTHORIZED_USER - 인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "USER_NOT_FOUND - 사용자를 찾을 수 없음")
    })
    public ApiResponse<UserDTO.UserResponseDTO> loginWithGoogle(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        UserDTO.UserResponseDTO loginResponse = authService.loginWithGoogleCode(code, response);
        log.info("[AUTH] 구글 로그인 성공 userId={}", loginResponse.id());
        return ApiResponse.onSuccess(loginResponse,SuccessCode.OK);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "AccessToken을 쿠키에서 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "UNAUTHORIZED_USER - 인증되지 않은 사용자")
    })
    public ApiResponse<Void> logout(HttpServletResponse response, @CurrentUser CustomUserDetails userDetails) {
        authService.logout(userDetails.getUser().getId(), response);
        return ApiResponse.onSuccess(null, SuccessCode.OK);
    }

}
