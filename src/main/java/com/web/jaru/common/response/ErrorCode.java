package com.web.jaru.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode {

    // 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-001", "서버 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-002", "요청 파라미터가 올바르지 않습니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "DATE-001", "유효하지 않은 날짜입니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "FORMAT-001", "형식이 올바르지 않습니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "COMMON-003", "JSON 파싱에 실패했습니다."),

    // 자격증 에러
    CERTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "CERT-001", "해당 자격증이 존재하지 않습니다."),
    EXIST_USER_SCRAP_CERT(HttpStatus.CONFLICT, "CERT-002", "이미 존재하는 자격증 스크랩 정보입니다."),
    NOT_FOUND_USER_SCRAP_CERT(HttpStatus.NO_CONTENT, "CERT-003", "존재하지 않는 자격증 스크랩 정보입니다."),
    CERT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CERT-004", "존재하지 않는 자격증 카테고리입니다."),

    // 커뮤니티 에러
    POST_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-001", "존재하지 않는 게시글 카테고리입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-002", "존재하지 않는 게시글입니다."),
    PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "POST-003","권한이 없습니다."),
    EXIST_POST_LIKE(HttpStatus.CONFLICT, "POST-004", "이미 존재하는 좋아요 정보입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "존재하지 않는 댓글입니다."),
    COMMENT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMENT-002", "유효하지 않은 댓글입니다."),
    EXIST_POLL_BY_POST(HttpStatus.CONFLICT, "POLL-001", "이미 존재하는 투표입니다."),
    POLL_NOT_FOUND(HttpStatus.NOT_FOUND, "POLL404", "해당 투표를 찾을 수 없습니다."),
    POLL_MAX_SELECTION_EXCEEDED(HttpStatus.BAD_REQUEST,"POLL400", "선택 가능한 투표 옵션 개수는 최대 3개입니다."),
    INVALID_POLL_OPTION(HttpStatus.BAD_REQUEST,"POLL400", "투표옵션의 소속 투표가 올바르지 않습니다."),

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT404", "사용자를 찾을 수 없습니다."),
    USER_INACTIVE(HttpStatus.FORBIDDEN,"ACCOUNT403", "탈퇴한 사용자입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCOUNT401", "토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT404","리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCOUNT401","리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "ACCOUNT400","리프레시 토큰이 유효하지 않습니다."),
    INVALID_OAUTH_TOKEN(HttpStatus.BAD_REQUEST, "ACCOUNT400", "유효하지 않는 카카오/구글/네이버 토큰입니다." ),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED,"ACCOUNT401" ,"인증된 사용자가 없습니다." ),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "ACCOUNT 404", "해당 사용자를 찾을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
