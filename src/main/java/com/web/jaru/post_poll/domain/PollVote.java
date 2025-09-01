package com.web.jaru.post_poll.domain;

import com.web.jaru.BaseTimeEntity;
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
@Table(name = "poll_votes",
        uniqueConstraints = @UniqueConstraint(name="uk_vote_unique", columnNames={"user_id","poll_id","poll_option_id"}))
public class PollVote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_vote_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="poll_id", nullable=false)
    private Poll poll;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="option_id", nullable=false)
    private PollOption option;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false)
    private User user;
}
