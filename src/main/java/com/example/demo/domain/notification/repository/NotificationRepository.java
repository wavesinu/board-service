package com.example.demo.domain.notification.repository;

import com.example.demo.domain.notification.entity.Notification;
import com.example.demo.domain.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // 사용자의 읽지 않은 알림 수 조회
    long countByReceiverIdAndStatus(Long receiverId, NotificationStatus status);
    
    // 사용자의 알림 목록 페이징 조회
    Page<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);
    
    // 특정 사용자의 모든 알림을 읽음 처리
    @Modifying
    @Query("UPDATE Notification n SET n.status = :status " +
           "WHERE n.receiver.id = :receiverId AND n.status = 'UNREAD'")
    void markAllAsRead(@Param("receiverId") Long receiverId, 
                      @Param("status") NotificationStatus status);
}
