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
    private String title;
    private Long user_id;
    private String username;
    private String profile_url;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long like;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.post_id = comment.getPost().getId();
        this.title = comment.getPost().getTitle();
        this.user_id = comment.getUser().getId();
        this.username = comment.getUser().getUsername();
        this.profile_url = comment.getUser().getProfileUrl();
        this.nickname = comment.getUser().getNickname();
        this.like = comment.getGoodLike();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();

    }
}
