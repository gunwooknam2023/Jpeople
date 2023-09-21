package com.gunwook.jpeople.post.entity;

import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.common.TimeStamped;
import com.gunwook.jpeople.goodlike.entity.GoodLike;
import com.gunwook.jpeople.post.dto.PostRequestDto;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "posts")
public class Post extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "likes")
    private Long goodLike = 0L;

    @Column(name = "view")
    private Long viewCnt = 0L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<GoodLike> goodLikeList = new ArrayList<>();


    public Post(PostRequestDto postRequestDto, User user){
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.user = user;
    }

    public void updatePost(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void likePost(){
        this.goodLike++;
    }

    public void deleteLikePost(){
        this.goodLike--;
    }

    public void viewCnt(){
        this.viewCnt++;
    }
}
