package com.gunwook.jpeople.schedulecard.dto;

import com.gunwook.jpeople.schedulecard.entity.ScheduleCard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleCardResponseDto {
    private Long id;
    private String todo;
    private Boolean health;
    private Long user_id;
    private Long schedulePost_id;
    private LocalDateTime createdAt;

    public ScheduleCardResponseDto(ScheduleCard scheduleCard){
        this.id = scheduleCard.getId();
        this.todo = scheduleCard.getTodo();
        this.health = scheduleCard.getHealth();
        this.user_id = scheduleCard.getUser().getId();
        this.schedulePost_id = scheduleCard.getSchedulePost().getId();
        this.createdAt = scheduleCard.getCreatedAt();
    }
}
