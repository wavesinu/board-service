package com.example.demo.domain.post.entity;

import org.springframework.data.jpa.repository.Lock;

import com.example.demo.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import jakarta.persistence.LockModeType;

@Entity
@Table(name = "post_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostStat extends BaseEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false)
    private Long commentCount = 0L;

    @Version
    private Long version;  // 낙관적 락

    public PostStat(Post post) {
        this.post = post;
    }

    // 동시성을 고려한 카운터 증감
    @Lock(LockModeType.OPTIMISTIC)
    public void incrementViewCount() {
        this.viewCount++;
    }

    @Lock(LockModeType.OPTIMISTIC)
    public void incrementLikeCount() {
        this.likeCount++;
    }

    @Lock(LockModeType.OPTIMISTIC)
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    @Lock(LockModeType.OPTIMISTIC)
    public void incrementCommentCount() {
        this.commentCount++;
    }

    @Lock(LockModeType.OPTIMISTIC)
    public void decrementCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }
}
