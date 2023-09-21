package com.gunwook.jpeople.goodlike.entity;

import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "likes")
public class GoodLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public GoodLike(User user, Post post){
        this.user = user;
        this.post = post;
    }

    public GoodLike(User user, Comment comment){
        this.user = user;
        this.comment = comment;
    }






}
