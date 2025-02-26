package com.example.demo.domain.comment.entity;

import com.example.demo.domain.BaseEntity;
import com.example.demo.domain.enums.CommentStatus;
import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments",
       indexes = {
           @Index(name = "idx_post_path", columnList = "post_id, path"),
           @Index(name = "idx_parent_id", columnList = "parent_id")
       })
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(length = 1000, nullable = false)
    private String path;

    @Column(nullable = false)
    private int depth;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status = CommentStatus.ACTIVE;

    @Version
    private Long version;  // 낙관적 락

    @Builder
    public Comment(Post post, User user, Comment parent, String content) {
        this.post = post;
        this.user = user;
        this.parent = parent;
        this.content = content;
        this.status = CommentStatus.ACTIVE;

        if (parent == null) {
            this.depth = 0;
        } else {
            this.depth = parent.getDepth() + 1;
            validateDepth();
        }
    }

    // 댓글 내용 수정
    public void update(String content) {
        validateStatus();
        this.content = content;
    }

    // 댓글 삭제
    public void delete() {
        this.status = CommentStatus.DELETED;
    }

    // 댓글 차단
    public void block() {
        this.status = CommentStatus.BLOCKED;
    }

    // 경로 업데이트 (저장 후 호출)
    public void updatePath() {
        if (this.id == null) {
            throw new IllegalStateException("댓글이 저장된 후에 경로를 설정할 수 있습니다.");
        }
        
        if (this.parent == null) {
            this.path = String.format("%020d", this.id);
        } else {
            this.path = this.parent.getPath() + String.format("%020d", this.id);
        }
    }

    // 수정 가능 여부 확인
    public boolean isEditable() {
        return this.status == CommentStatus.ACTIVE;
    }

    // 특정 사용자의 댓글인지 확인
    public boolean isOwnedBy(Long userId) {
        return this.user.getId().equals(userId);
    }

    // 댓글 깊이 검증
    private void validateDepth() {
        if (this.depth > 2) {  // 최대 2단계까지만 허용
            throw new IllegalStateException("댓글은 2단계까지만 작성할 수 있습니다.");
        }
    }

    // 상태 검증
    private void validateStatus() {
        if (this.status != CommentStatus.ACTIVE) {
            throw new IllegalStateException("삭제되거나 차단된 댓글은 수정할 수 없습니다.");
        }
    }
}
