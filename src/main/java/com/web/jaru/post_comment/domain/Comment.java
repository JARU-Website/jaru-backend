package com.web.jaru.post_comment.domain;

import com.web.jaru.BaseTimeEntity;
import com.web.jaru.posts.domain.Post;
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
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Builder.Default
    private int likeCount = 0;

    @Builder.Default
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // null 이면 루트 댓글

    @Builder.Default
    private int replyCount = 0; // 대댓글 수 (루트 댓글에만 의미 있음)

    public boolean isRoot(){ return parent == null; }

    // 변경 메서드
    public void changeContent(String content) { this.content = content; }

    public void plusLikeCount() { this.likeCount++; }

    public void minusLikeCount() { this.likeCount--; }

    public void plusReplyCount() { this.replyCount++; }

    public void minusReplyCount() { this.replyCount--; }

    public void softDelete() { this.isDeleted = true; this.content = ""; }
}
