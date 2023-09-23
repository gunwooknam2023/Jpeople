package com.gunwook.jpeople.schedulecard.repository;

import com.gunwook.jpeople.schedulecard.entity.ScheduleCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleCardRepository extends JpaRepository<ScheduleCard, Long> {
    List<ScheduleCard> findBySchedulePostId(Long schedulePostId);

}
