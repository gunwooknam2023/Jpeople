package com.gunwook.jpeople.comment.dto;

import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String contents;
    private Long post_id;
    private Long user_id;
    private String username;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long like;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.post_id = comment.getPost().getId();
        this.user_id = comment.getUser().getId();
        this.username = comment.getUser().getUsername();
        this.nickname = comment.getUser().getNickname();
        this.like = comment.getGoodLike();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();

    }
}
