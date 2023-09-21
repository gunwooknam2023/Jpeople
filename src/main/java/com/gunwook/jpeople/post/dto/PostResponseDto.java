package com.gunwook.jpeople.post.dto;

import com.gunwook.jpeople.comment.dto.CommentResponseDto;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
    private Long user_id;
    private String username;
    private String nickname;
    private Long like;
    private Long viewCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> commentResponseDtoList;


    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.user_id = post.getUser().getId();
        this.username = post.getUser().getUsername();
        this.nickname = post.getUser().getNickname();
        this.like = post.getGoodLike();
        this.viewCnt = post.getViewCnt();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentResponseDtoList = post.getCommentList().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .toList();

    }
}


