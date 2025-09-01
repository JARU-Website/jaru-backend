package com.web.jaru.post_poll;

import com.web.jaru.BaseTimeEntity;
import com.web.jaru.posts.domain.Post;
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
@Table(name = "polls")
public class Poll extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long id;

    private String title;

    @Builder.Default
    private int totalVoteCount = 0; // 총 득표수

    private boolean allowMultiple; // 중복 투표 혀용

    @OneToOne(fetch = FetchType.LAZY) // 게시글당 투표 1개라면
    @JoinColumn(name="post_id", nullable=false, unique=true)
    private Post post;

    @OneToMany(mappedBy="poll", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderBy("idx ASC")
    private List<PollOption> options = new ArrayList<>();


}
