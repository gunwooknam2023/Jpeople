package com.gunwook.jpeople.schedulePost.dto;

import lombok.Getter;

@Getter
public class ScheduleManagerResponseDto {
    private Double todayPercent;
    private Double monthPercent;
    private Double allPercent;

    public ScheduleManagerResponseDto(Double todayPercent, Double monthPercent, Double allPercent){
        this.todayPercent = todayPercent;
        this.monthPercent = monthPercent;
        this.allPercent = allPercent;
    }
}
