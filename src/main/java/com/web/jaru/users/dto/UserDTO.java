package com.web.jaru.users.dto;

import com.web.jaru.users.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class UserDTO {

    @Builder
    public record UserResponseDTO(
            @Schema(description = "사용자 ID", example = "1")
            Long id,

            @Schema(description = "사용자 이메일", example = "user@example.com")
            String email,

            @Schema(description = "닉네임", example = "user")
            String nickname

    ) {
        public static UserResponseDTO from(User user) {
            return UserResponseDTO.builder()
                    .id(user.getId( ))
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
        }
    }
}
