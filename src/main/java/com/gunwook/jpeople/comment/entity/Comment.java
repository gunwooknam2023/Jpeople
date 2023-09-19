package com.gunwook.jpeople.comment.entity;

import com.gunwook.jpeople.comment.dto.CommentRequestDto;
import com.gunwook.jpeople.common.TimeStamped;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "comments")
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDto commentRequestDto, User user, Post post){
        this.contents = commentRequestDto.getContents();
        this.user = user;
        this.post = post;
    }

    public void updateComment(CommentRequestDto commentRequestDto){
        this.contents = commentRequestDto.getContents();
    }


}
