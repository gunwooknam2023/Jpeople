package com.gunwook.jpeople.alarm.dto;

import com.gunwook.jpeople.alarm.entity.Alarm;
import lombok.Getter;

@Getter
public class AlarmResponseDto {
    private Long id;
    private Long user_id;
    private String contents;
    private String address;
    private boolean read;

    public AlarmResponseDto(Alarm alarm){
        this.id = alarm.getId();
        this.user_id = alarm.getUser().getId();
        this.contents = alarm.getContents();
        this.address = alarm.getAddress();
        this.read = alarm.isRead();
    }
}
