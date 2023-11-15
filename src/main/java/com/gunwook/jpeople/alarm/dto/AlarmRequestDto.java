package com.gunwook.jpeople.alarm.dto;

import com.gunwook.jpeople.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmRequestDto {
    private String contents;
    private String address;
    private boolean check;
    private Long user_id;
}
