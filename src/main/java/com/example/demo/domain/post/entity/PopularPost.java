package com.example.demo.domain.post.entity;

import com.example.demo.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "popular_posts")
public class PopularPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private Long score;

    public PopularPost(Long postId, Long score) {
        this.postId = postId;
        this.score = score;
    }

    public void updateScore(Long newScore) {
        this.score = newScore;
    }
}
