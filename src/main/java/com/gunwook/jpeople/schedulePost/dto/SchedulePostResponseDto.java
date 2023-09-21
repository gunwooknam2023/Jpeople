package com.gunwook.jpeople.schedulePost.dto;

import com.gunwook.jpeople.schedulePost.entity.SchedulePost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SchedulePostResponseDto {
    private Long id;
    private String title;
    private String promise;
    private Long user_id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SchedulePostResponseDto(SchedulePost schedulePost){
        this.id = schedulePost.getId();
        this.title = schedulePost.getTitle();
        this.promise = schedulePost.getPromise();
        this.user_id = schedulePost.getUser().getId();
        this.createdAt = schedulePost.getCreatedAt();
        this.modifiedAt = schedulePost.getModifiedAt();
    }
}
