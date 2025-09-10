package com.web.jaru.user_scrap_cert.domain;

import com.web.jaru.BaseTimeEntity;
import com.web.jaru.certifications.domain.Certification;
import com.web.jaru.users.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_scrap_cert", uniqueConstraints = {@UniqueConstraint(
        name = "user_cert_unique",
        columnNames = {"user_id", "certification_id"}
)})
@SoftDelete(columnName = "is_deleted")
public class UserScrapCert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_scrap_cert_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Certification certification;

}
