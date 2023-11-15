package com.gunwook.jpeople.alarm.entity;

import com.gunwook.jpeople.alarm.dto.AlarmRequestDto;
import com.gunwook.jpeople.common.TimeStamped;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "alarms")
public class Alarm extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "check", nullable = false)
    private boolean check;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Alarm(AlarmRequestDto alarmRequestDto, User user){
        this.contents = alarmRequestDto.getContents();
        this.address = alarmRequestDto.getAddress();
        this.check = alarmRequestDto.isCheck();
        this.user = user;
    }

    public void updateCheck(){
        this.check = true;
    }

}
