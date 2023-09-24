package com.gunwook.jpeople.schedulePost.dto;

import com.gunwook.jpeople.comment.dto.CommentResponseDto;
import com.gunwook.jpeople.schedulePost.entity.SchedulePost;
import com.gunwook.jpeople.schedulecard.dto.ScheduleCardResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
public class SchedulePostResponseDto {
    private Long id;
    private String title;
    private String promise;
    private Long user_id;
    private List<ScheduleCardResponseDto> scheduleCardResponseDtos;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SchedulePostResponseDto(SchedulePost schedulePost){
        this.id = schedulePost.getId();
        this.title = schedulePost.getTitle();
        this.promise = schedulePost.getPromise();
        this.user_id = schedulePost.getUser().getId();
        this.createdAt = schedulePost.getCreatedAt();
        this.modifiedAt = schedulePost.getModifiedAt();
        this.scheduleCardResponseDtos = schedulePost.getScheduleCardList().stream()
                        .map(ScheduleCardResponseDto::new).sorted(Comparator.comparing(ScheduleCardResponseDto::getCreatedAt).reversed())
                        .toList();
    }
}
