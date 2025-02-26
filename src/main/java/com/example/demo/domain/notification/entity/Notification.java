package com.example.demo.domain.notification.entity;

import com.example.demo.domain.BaseEntity;
import com.example.demo.domain.enums.NotificationStatus;
import com.example.demo.domain.enums.NotificationType;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications", 
       indexes = {
           @Index(name = "idx_receiver_status", columnList = "receiver_id, status"),
           @Index(name = "idx_receiver_created_at", columnList = "receiver_id, created_at")
       })
@Getter
@NoArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;              // 알림을 받는 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;                // 알림을 발생시킨 사용자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;      // 알림 유형

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;  // 알림 상태

    @Column(nullable = false)
    private String title;               // 알림 제목

    @Column(nullable = false)
    private String content;             // 알림 내용

    @Column(name = "target_id")
    private Long targetId;              // 대상 ID (게시글 ID 또는 댓글 ID)

    @Column(name = "target_url")
    private String targetUrl;           // 이동할 URL

    @Builder
    public Notification(User receiver, User sender, NotificationType type, 
                       String title, String content, Long targetId, String targetUrl) {
        this.receiver = receiver;
        this.sender = sender;
        this.type = type;
        this.status = NotificationStatus.UNREAD;
        this.title = title;
        this.content = content;
        this.targetId = targetId;
        this.targetUrl = targetUrl;
    }

    public void markAsRead() {
        this.status = NotificationStatus.READ;
    }
}
