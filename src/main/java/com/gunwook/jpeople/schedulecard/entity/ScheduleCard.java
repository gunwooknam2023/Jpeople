package com.gunwook.jpeople.schedulecard.entity;

import com.gunwook.jpeople.schedulePost.entity.SchedulePost;
import com.gunwook.jpeople.schedulecard.dto.ScheduleCardRequestDto;
import com.gunwook.jpeople.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "schedulecards")
public class ScheduleCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String todo;

    @Column(nullable = false)
    private Boolean health = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedulepost_id")
    private SchedulePost schedulePost;

    public ScheduleCard(ScheduleCardRequestDto scheduleCardRequestDto, User user, SchedulePost schedulePost){
        this.todo = scheduleCardRequestDto.getTodo();
        this.user = user;
        this.schedulePost = schedulePost;
    }

    public void updateCard(ScheduleCardRequestDto scheduleCardRequestDto){
        this.todo = scheduleCardRequestDto.getTodo();
    }

    public void trueHealth(){
        this.health = true;
    }

    public void falseHealth(){
        this.health = false;
    }
}
