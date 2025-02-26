package com.example.demo.domain.post.entity;

import com.example.demo.domain.BaseEntity;
import com.example.demo.domain.enums.PostStatus;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts",
       indexes = {
           @Index(name = "idx_user_created_at", columnList = "user_id, created_at"),
           @Index(name = "idx_status_created_at", columnList = "status, created_at")
       })
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.DRAFT;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostStat postStat;

    @Version
    private Long version;  // 낙관적 락을 위한 버전 필드

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.postStat = new PostStat(this);
    }

    // 내용 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 발행 상태 변경
    public void changeStatus(PostStatus status) {
        this.status = status;
    }

    // 게시글 삭제
    public void delete() {
        this.status = PostStatus.DELETED;
    }

    // 게시글 발행
    public void publish() {
        this.status = PostStatus.PUBLISHED;
    }

    // 게시글이 특정 사용자의 것인지 확인
    public boolean isOwnedBy(Long userId) {
        return this.user.getId().equals(userId);
    }

    // 게시글이 수정 가능한 상태인지 확인
    public boolean isEditable() {
        return this.status != PostStatus.DELETED;
    }
}
