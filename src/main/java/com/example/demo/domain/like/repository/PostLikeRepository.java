package com.example.demo.domain.like.repository;

import com.example.demo.domain.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    // 좋아요 조회 최적화
    @Query("SELECT pl FROM PostLike pl " +
           "WHERE pl.post.id = :postId AND pl.user.id = :userId")
    Optional<PostLike> findByPostIdAndUserId(
            @Param("postId") Long postId, 
            @Param("userId") Long userId);

    // 게시글의 좋아요 목록 조회 (페이징)
    @Query("SELECT pl FROM PostLike pl " +
           "JOIN FETCH pl.user " +
           "WHERE pl.post.id = :postId " +
           "ORDER BY pl.createdAt DESC")
    List<PostLike> findLikesByPostId(@Param("postId") Long postId);

    // 게시글 삭제 시 좋아요 일괄 삭제 (벌크 연산)
    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.post.id = :postId")
    int deleteAllByPostId(@Param("postId") Long postId);

    // 기존 메서드들
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
}
