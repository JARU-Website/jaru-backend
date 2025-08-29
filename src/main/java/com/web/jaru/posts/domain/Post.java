package com.web.jaru.posts.domain;

import com.web.jaru.BaseTimeEntity;
import com.web.jaru.certifications.domain.CertCategory;
import com.web.jaru.users.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 255)
    private String title;

    @Column(length = 3000)
    private String content;

    @Builder.Default
    private int likeCount = 0; // 추천수

    @Builder.Default
    private int view = 0; // 조회수

    @Builder.Default
    private int commentCount = 0; // 댓글수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_category_id") // FK 제약이 없어도 무방, 있으면 그대로 사용
    private PostCategory postCategory;

    @ManyToOne(fetch = FetchType.LAZY, optional = true) // category_id NULL 허용
    @JoinColumn(name = "cert_category_id") // FK 제약이 없어도 무방, 있으면 그대로 사용
    private CertCategory certCategory;

    private void plusLikeCount() { this.likeCount++; }

    private void minusLikeCount() { this.likeCount--; }

}
