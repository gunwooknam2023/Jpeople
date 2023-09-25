package com.gunwook.jpeople.schedulePost.entity;

import com.gunwook.jpeople.common.TimeStamped;
import com.gunwook.jpeople.schedulePost.dto.SchedulePostRequestDto;
import com.gunwook.jpeople.schedulecard.entity.ScheduleCard;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "schedules")
public class SchedulePost extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "promise", nullable = false)
    private String promise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "schedulePost", cascade = CascadeType.REMOVE)
    private List<ScheduleCard> scheduleCardList = new ArrayList<>();

    public SchedulePost(SchedulePostRequestDto schedulePostRequestDto, User user){
        this.title = schedulePostRequestDto.getTitle();
        this.promise = schedulePostRequestDto.getPromise();
        this.user = user;
    }

    public void updateSchedulePost(SchedulePostRequestDto schedulePostRequestDto){
        this.title = schedulePostRequestDto.getTitle();
        this.promise = schedulePostRequestDto.getPromise();
    }

}