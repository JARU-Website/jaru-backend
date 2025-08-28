package com.web.jaru.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseCode {

    OK(HttpStatus.OK, "SUCCESS-200", "요청에 성공했습니다."),
    CREATED(HttpStatus.CREATED, "SUCCESS-201", "리소스가 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED, "SUCCESS-202", "요청을 접수했습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "SUCCESS-204", "콘텐츠가 없습니다."),

    // --- Domain-specific success codes ---
    CERT_SCRAP_SAVED(HttpStatus.CREATED, "CERT-SUCCESS-201-01", "스크랩 정보가 저장되었습니다."),
    CERT_SCRAP_DELETED(HttpStatus.NO_CONTENT, "CERT-SUCCESS-204-01", "스크랩이 삭제되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
