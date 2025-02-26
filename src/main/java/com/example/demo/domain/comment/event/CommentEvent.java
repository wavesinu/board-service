package com.example.demo.domain.comment.event;

import com.example.demo.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public abstract class CommentEvent {

    /* 모든 댓글 관련 event의 기본 클래스 */
    private final Comment comment;
    
    protected CommentEvent(Comment comment) {
        this.comment = comment;
    }
}