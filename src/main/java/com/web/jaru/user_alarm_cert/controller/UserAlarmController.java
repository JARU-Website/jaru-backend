package com.web.jaru.user_alarm_cert.controller;

import com.web.jaru.common.annotation.CurrentUser;
import com.web.jaru.common.response.ApiResponse;
import com.web.jaru.common.response.SuccessCode;
import com.web.jaru.security.service.CustomUserDetails;
import com.web.jaru.user_alarm_cert.service.UserAlramCertService;
import com.web.jaru.users.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certifications/alarms")
@Tag(name = "자격증 알림 설정 API", description = "자격증 알림 관련 API")
public class UserAlarmController {

    private final UserAlramCertService userAlramCertService;

    @PostMapping("/{certId}")
    @Operation(summary = "자격증 알림 설정", description = "특정 자격증에 대해 알림을 설정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "200", description = "알림 설정 성공"),
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "404", description = "CERTIFICATION_NOT_FOUND - 자격증을 찾을 수 없음")
    })
    public ApiResponse<Void> setCertAlarm(
            @CurrentUser User user,
            @PathVariable Long certId) {

        userAlramCertService.setCertAlarm(user, certId);
        return ApiResponse.onSuccess(null, SuccessCode.OK);
    }

    @DeleteMapping("/{certId}")
    @Operation(summary = "자격증 알림 취소", description = "특정 자격증의 알림을 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "200", description = "알림 취소 성공"),
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "404", description = "CERTIFICATION_NOT_FOUND - 자격증을 찾을 수 없음")
    })
    public ApiResponse<Void> cancelCertAlarm(
            @CurrentUser User user,
            @PathVariable Long certId) {
        userAlramCertService.cancelCertAlarm(user, certId);
        return ApiResponse.onSuccess(null, SuccessCode.OK);
    }
}

