package com.example.demo.domain.like.entity;

import com.example.demo.domain.comment.entity.Comment;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment_likes",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"user_id", "comment_id"})
       },
       indexes = {
           @Index(name = "idx_comment_user", columnList = "comment_id, user_id")
       })
@Getter
@NoArgsConstructor
public class CommentLike extends BaseLike {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    public CommentLike(User user, Comment comment) {
        super(user);
        this.comment = comment;
    }

    public boolean isLikedBy(Long userId) {
        return this.getUser().getId().equals(userId);
    }
}
