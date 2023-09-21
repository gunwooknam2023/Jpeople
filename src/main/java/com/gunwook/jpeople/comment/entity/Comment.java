package com.gunwook.jpeople.comment.entity;

import com.gunwook.jpeople.comment.dto.CommentRequestDto;
import com.gunwook.jpeople.common.TimeStamped;
import com.gunwook.jpeople.goodlike.entity.GoodLike;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "likes")
    private Long goodLike = 0L;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<GoodLike> goodLikeList = new ArrayList<>();

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

    public void likeComment(){
        this.goodLike++;
    }

    public void deleteLikeComment(){
        this.goodLike--;
    }


}
