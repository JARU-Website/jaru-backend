package com.web.jaru.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.jaru.common.exception.CustomException;
import com.web.jaru.common.response.ErrorCode;
import com.web.jaru.security.redis.TokenRedis;
import com.web.jaru.security.redis.TokenRedisRepository;
import com.web.jaru.security.service.JwtProvider;
import com.web.jaru.users.domain.Status;
import com.web.jaru.users.domain.User;
import com.web.jaru.users.dto.UserDTO;
import com.web.jaru.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuthService {

    private final TokenRedisRepository tokenRedisRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final JwtProvider jwtProvider;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer ";

    @Value("${auth.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${auth.google.client-id}")
    private String googleClientId;

    @Value("${auth.google.client-secret}")
    private String googleClientSecret;

    @Value("${auth.google.token-request-uri}")
    private String googleTokenRequestUri;

    @Value("${auth.google.member-info-request-uri}")
    private String googleMemberInfoRequestUri;

    /**
     * Google 토큰 받아오기
     */
    public OAuthDTO.GoogleOAuthTokenResponse getGoogleToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        OAuthDTO.GoogleOAuthRequestParams params = new OAuthDTO.GoogleOAuthRequestParams(
                "authorization_code",
                googleClientId,
                googleClientSecret,
                googleRedirectUri,
                code
        );

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", params.grantType());
        body.add("client_id", params.clientId());
        body.add("client_secret", params.clientSecret());
        body.add("redirect_uri", params.redirectUri());
        body.add("code", params.code());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(googleTokenRequestUri, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return objectMapper.readValue(response.getBody(), OAuthDTO.GoogleOAuthTokenResponse.class);
            }
        } catch (Exception e) {
            log.error("구글 토큰 요청 실패", e);
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }

        throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
    }

    /**
     * 구글 AccessToken으로 사용자 정보 요청
     */
    public Map<String, Object> getUserAttributes(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(AUTHORIZATION_HEADER, TOKEN_TYPE + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
            return restTemplate.exchange(
                    googleMemberInfoRequestUri,
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            ).getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("구글 사용자 정보 요청 실패", e);
            throw new CustomException(ErrorCode.INVALID_OAUTH_TOKEN);
        }
    }

    /**
     * 사용자 조회 및 생성
     */
    public User saveOrGetUser(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            log.info("[LOGIN] 기존 사용자 로그인 email={}", email);
            return findUser.get();
        }

        // 신규 사용자 생성
        User newUser = User.builder()
                .email(email)
                .nickname(name != null ? name : email)
                .role("USER")
                .status(Status.ACTIVE)
                .build();

        userRepository.save(newUser);
        log.info("[SIGNUP] 신규 사용자 생성 email={}", email);
        return newUser;
    }

    /**
     * 구글 로그인 처리
     */
    public UserDTO.UserResponseDTO loginWithGoogleCode(String code, HttpServletResponse response) {
        // 토큰 요청
        OAuthDTO.GoogleOAuthTokenResponse tokenResponse = getGoogleToken(code);
        // 사용자 정보 조회
        Map<String, Object> attributes = getUserAttributes(tokenResponse.accessToken());
        // 사용자 저장
        User user = saveOrGetUser(attributes);
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken();

        TokenRedis token = TokenRedis.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        // 레디스에 토큰 저장
        tokenRedisRepository.save(token);
        // 쿠키에 엑세스 토큰 저장
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
//                                .secure(true)
//                                .sameSite("None")
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        log.info("[LOGIN] 토큰 저장 성공 accessToken={}", accessToken);
        // 6. 로그인 결과 반환 DTO
        return UserDTO.UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }


    /**
     * 로그아웃
     * - 현재 로그인한 사용자의 RefreshToken 삭제
     * - SecurityContext 초기화
     */
    public void logout(Long userId) {
        try {
            tokenRedisRepository.deleteByUserId(userId);
            log.info("[LOGOUT] userId={} 의 RefreshToken 삭제 완료", userId);

            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            log.error("[LOGOUT] 로그아웃 처리 실패 userId={}", userId, e);
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}
