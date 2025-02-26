package com.example.demo.domain.enums;

public enum NotificationType {
    POST_COMMENT,        // 게시글에 댓글이 달림
    COMMENT_REPLY,      // 댓글에 답글이 달림
    POST_LIKE,          // 게시글에 좋아요
    COMMENT_LIKE,       // 댓글에 좋아요
    MENTION             // @멘션
} 