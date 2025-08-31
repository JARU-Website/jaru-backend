package com.web.jaru.security.filter;

import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.security.service.CustomUserDetails;
import com.web.jaru.users.domain.User;
import com.web.jaru.security.redis.TokenRedis;
import com.web.jaru.security.redis.TokenRedisRepository;
import com.web.jaru.users.repository.UserRepository;
import com.web.jaru.security.service.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final TokenRedisRepository tokenRedisRepository;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = resolveTokenFromCookie(request);
        // 엑세스 토큰 검증
        if (accessToken != null) {
            if (jwtProvider.validateToken(accessToken)) {
                // 만료되지 않으면 SecurityContext 에 저장
                setAuthentication(accessToken);
            } else {
                // 액세스 토큰 만료 시 레디스에서 리프레시 토큰 조회
                tokenRedisRepository.findByAccessToken(accessToken).ifPresent(tokenRedis -> {
                    String refreshToken = tokenRedis.getRefreshToken();
                    if (jwtProvider.validateToken(refreshToken)) {
                        // 리프레시 토큰 유효할 시  새 액세스 토큰 발급
                        User user = userRepository.findById(tokenRedis.getId())
                                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                        String newAccessToken = jwtProvider.createAccessToken(user);
                        // Redis 업데이트, 리프레시 토큰 유지
                        TokenRedis newTokenRedis = TokenRedis.builder()
                                .id(user.getId())
                                .refreshToken(refreshToken)
                                .accessToken(newAccessToken)
                                .build();
                        tokenRedisRepository.save(newTokenRedis);

                        // 쿠키 갱신, 유효시간을 1시간으로 설정
                        ResponseCookie cookie = ResponseCookie.from("accessToken", newAccessToken)
                                .httpOnly(true)
//                                .secure(true)
//                                .sameSite("None")
                                .secure(false)
                                .sameSite("Lax")
                                .path("/")
                                .maxAge(Duration.ofHours(1))
                                .build();
                        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                        log.info("[TOKEN] 토큰 만료, 새로운 토큰 발급 ={}", newAccessToken);
                        setAuthentication(newAccessToken);
                    }
                });
            }
        }

        filterChain.doFilter(request, response);
    }

    // 쿠키 추출
    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // accessToken 추출
    private String resolveTokenFromCookie(HttpServletRequest request) {
        return extractCookie(request, "accessToken");
    }

    private void setAuthentication(String accessToken) {
        String email = jwtProvider.extractEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}