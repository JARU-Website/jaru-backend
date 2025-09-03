package com.web.jaru.users.domain;

import com.web.jaru.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 15)
    private String phoneNum;

    @Column(length = 50)
    private String nickname;

    @Column(length = 20)
    private String role = "USER";

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(length = 30)
    private String authProvider;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_notified")
    private boolean isNotified = false;

    @Column(name = "is_googlecal_connected")
    private boolean isGoogleCalConnected = false;


}
