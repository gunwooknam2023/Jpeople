package com.gunwook.jpeople.report.entity;

import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.report.dto.ReportRequestDto;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String reason;

//    @Column(name = "comment_id")
//    private Long comment_id;
//
//    @Column(name = "postUser_id")
//    private Long postUser_id;
//
//    @Column(name = "CommentUser_id")
//    private Long commentUser_id;

//    @Column(name = "reporter_id", nullable = false)
//    private Long reporter_id;

    public Report(Post post, User user, ReportRequestDto reportRequestDto) {
        this.post = post;
        this.user = user;
        this.reason = reportRequestDto.getReason();
    }
}
