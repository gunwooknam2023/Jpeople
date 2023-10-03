package com.gunwook.jpeople.report.dto;

import com.gunwook.jpeople.comment.dto.CommentResponseDto;
import com.gunwook.jpeople.post.entity.Category;
import com.gunwook.jpeople.report.entity.Report;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReportResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String reason;
    private Long user_id;
    private String username;
    private String nickname;
    private Long like;
    private Long viewCnt;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> commentResponseDtoList;

    public ReportResponseDto(Report report){
        this.id = report.getId();
        this.title = report.getPost().getTitle();
        this.contents = report.getPost().getContents();
        this.reason = report.getReason();
        this.user_id = report.getUser().getId();
        this.username = report.getUser().getUsername();
        this.nickname = report.getUser().getNickname();
        this.like = report.getPost().getGoodLike();
        this.viewCnt = report.getPost().getViewCnt();
        this.category = report.getPost().getCategory();
        this.createdAt = report.getPost().getCreatedAt();
        this.modifiedAt = report.getPost().getModifiedAt();
    }
}
