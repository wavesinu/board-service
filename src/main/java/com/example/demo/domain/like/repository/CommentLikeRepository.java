package com.example.demo.domain.like.repository;

import com.example.demo.domain.like.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    
    // 좋아요 조회 최적화
    @Query("SELECT cl FROM CommentLike cl " +
           "WHERE cl.comment.id = :commentId AND cl.user.id = :userId")
    Optional<CommentLike> findByCommentIdAndUserId(
            @Param("commentId") Long commentId, 
            @Param("userId") Long userId);

    // 댓글의 좋아요 목록 조회 (페이징)
    @Query("SELECT cl FROM CommentLike cl " +
           "JOIN FETCH cl.user " +
           "WHERE cl.comment.id = :commentId " +
           "ORDER BY cl.createdAt DESC")
    List<CommentLike> findLikesByCommentId(@Param("commentId") Long commentId);

    // 댓글 삭제 시 좋아요 일괄 삭제 (벌크 연산)
    @Modifying
    @Query("DELETE FROM CommentLike cl WHERE cl.comment.id = :commentId")
    int deleteAllByCommentId(@Param("commentId") Long commentId);

    // 기존 메서드들
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    void deleteByCommentIdAndUserId(Long commentId, Long userId);
    long countByCommentId(Long commentId);
}
