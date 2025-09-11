package com.web.jaru.post_poll.domain;

import com.web.jaru.BaseTimeEntity;
import com.web.jaru.posts.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "polls")
public class Poll extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Builder.Default
    private int totalVoteCount = 0; // 총 득표수

    private boolean allowMultiple; // 중복 투표 혀용

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable=false, unique=true)
    private Post post;

    @OneToMany(mappedBy="poll", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderColumn(name = "idx")
    @Builder.Default
    private List<PollOption> options = new ArrayList<>();

    /* --- 연관관계 메서드 --- */
    public void setPost(Post post) {
        this.post = post;
        post.setPoll(this);
    }

    /* --- 필드 수정 --- */
    public void changeTitle(String title) { this.title = title; }
    public void plusTotalVoteCount() { this.totalVoteCount++; }
    public void minusTotalVoteCount() {
        if (this.totalVoteCount > 0) this.totalVoteCount--;
    }
}
