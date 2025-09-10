package com.web.jaru.post_poll.domain;

import com.web.jaru.BaseTimeEntity;
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
@Table(name = "poll_options")
public class PollOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_option_id")
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="poll_id", nullable=false)
    private Poll poll;

    private String text; // 문항 내용

    @Builder.Default
    private int voteCount = 0;

    /* --- 연관관계 메서드 --- */
    public void setPoll(Poll poll) {
        this.poll = poll;
        poll.getOptions().add(this);
    }

    /* --- 필드 수정 --- */
    public void plusVoteCount() { this.voteCount++; }
    public void minusVoteCount() {
        if (this.voteCount > 0) this.voteCount--;
    }
}
