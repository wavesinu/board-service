package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.entity.Comment;
import com.example.demo.domain.enums.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 게시글의 최상위 댓글 조회 (페이징) - fetch join 추가
    @Query("SELECT DISTINCT c FROM Comment c " +
           "LEFT JOIN FETCH c.user " +
           "WHERE c.post.id = :postId " +
           "AND c.parent IS NULL " +
           "AND c.status != 'DELETED' " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findTopLevelComments(@Param("postId") Long postId, Pageable pageable);
    
    // 특정 댓글의 답글 조회 - fetch join 추가
    @Query("SELECT DISTINCT c FROM Comment c " +
           "LEFT JOIN FETCH c.user " +
           "WHERE c.parent.id = :parentId " +
           "AND c.status != 'DELETED' " +
           "ORDER BY c.path")
    List<Comment> findRepliesByParentId(@Param("parentId") Long parentId);
    
    // 커서 기반 페이징 (성능 최적화)
    @Query("SELECT c FROM Comment c " +
           "WHERE c.post.id = :postId " +
           "AND c.id < :lastCommentId " +
           "AND c.status != 'DELETED' " +
           "ORDER BY c.id DESC " +
           "LIMIT :pageSize")
    List<Comment> findByPostIdWithCursor(
            @Param("postId") Long postId,
            @Param("lastCommentId") Long lastCommentId,
            @Param("pageSize") int pageSize);

    // 게시글의 모든 댓글 상태 일괄 변경 (벌크 연산)
    @Modifying
    @Query("UPDATE Comment c " +
           "SET c.status = :status " +
           "WHERE c.post.id = :postId")
    int updateStatusByPostId(@Param("postId") Long postId, 
                           @Param("status") CommentStatus status);

    // 특정 사용자의 모든 댓글 상태 일괄 변경 (벌크 연산)
    @Modifying
    @Query("UPDATE Comment c " +
           "SET c.status = :status " +
           "WHERE c.user.id = :userId")
    int updateStatusByUserId(@Param("userId") Long userId, 
                           @Param("status") CommentStatus status);
    
    // 게시글의 모든 댓글 조회 (계층 구조 유지)
    @Query("SELECT c FROM Comment c " +
           "WHERE c.post.id = :postId " +
           "AND c.status != 'DELETED' " +
           "ORDER BY c.path")
    List<Comment> findAllByPostIdOrderByPath(@Param("postId") Long postId);
    
    // 특정 사용자의 댓글 조회
    Page<Comment> findByUserIdAndStatusNot(Long userId, CommentStatus status, Pageable pageable);
    
    // 게시글의 활성 댓글 수 조회
    long countByPostIdAndStatus(Long postId, CommentStatus status);
}
