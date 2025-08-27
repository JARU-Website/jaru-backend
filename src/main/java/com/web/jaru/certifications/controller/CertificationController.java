package com.web.jaru.certifications.controller;

import com.web.jaru.certifications.service.CertificationService;
import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cert")
@RequiredArgsConstructor
@RestController
public class CertificationController {

    private final CertificationService certificationService;

    // ======== 회원의 자격증 스크랩 =======

    // 회원-자격증 스크랩 관계 등록
    @PostMapping("/scrap/{certificationId}")
    public ApiResponse<Void> saveScrapInfo(User user,
                                           @PathVariable(value = "certificationId") Long certificationId) {
        certificationService.saveScrapInfo(user, certificationId);
        return ApiResponse.onSuccess(null, SuccessCode.CERT_SCRAP_SAVED);

    }
}
