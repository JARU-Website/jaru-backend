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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 3000)
    private String content;

    @Builder.Default
    private int likeCount = 0; // 추천수

    @Builder.Default
    private int view = 0; // 조회수

    @Builder.Default
    private int commentCount = 0; // 댓글수

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_category_id", nullable = false) // FK 제약이 없어도 무방, 있으면 그대로 사용
    private PostCategory postCategory;

    @ManyToOne(fetch = FetchType.LAZY) // category_id NULL 허용
    @JoinColumn(name = "cert_category_id") // FK 제약이 없어도 무방, 있으면 그대로 사용
    private CertCategory certCategory;

    public void plusLikeCount() { this.likeCount++; }

    public void minusLikeCount() { this.likeCount--; }

    // 필드 수정
    public void changeTitle(String title) { this.title = title; }
    public void changeContent(String content) { this.content = content; }
    public void changePostCategory(PostCategory postCategory) { this.postCategory = postCategory; }
    public void changeCertCategory(CertCategory certCategory) { this.certCategory = certCategory; }


}
