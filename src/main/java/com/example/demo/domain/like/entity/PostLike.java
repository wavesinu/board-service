package com.example.demo.domain.like.entity;

import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_likes",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"user_id", "post_id"})
       },
       indexes = {
           @Index(name = "idx_post_user", columnList = "post_id, user_id")
       })
@Getter
@NoArgsConstructor
public class PostLike extends BaseLike {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostLike(User user, Post post) {
        super(user);
        this.post = post;
    }

    public boolean isLikedBy(Long userId) {
        return this.getUser().getId().equals(userId);
    }
}
