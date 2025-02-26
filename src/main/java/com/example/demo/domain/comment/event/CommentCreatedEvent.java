package com.example.demo.domain.comment.event;

import com.example.demo.domain.comment.entity.Comment;

public class CommentCreatedEvent extends CommentEvent {

    /* 댓글 생성 시 발생하는 event(새로운 알림 생성 등) */
    public CommentCreatedEvent(Comment comment) {
        super(comment);
    }
} 