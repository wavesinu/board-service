package com.example.demo.domain.comment.event;

import com.example.demo.domain.comment.entity.Comment;

public class CommentDeletedEvent extends CommentEvent {

    /* 댓글 삭제 시 발생하는 event(관련 데이터 정리 등 트리거) */
    public CommentDeletedEvent(Comment comment) {
        super(comment);
    }
} 