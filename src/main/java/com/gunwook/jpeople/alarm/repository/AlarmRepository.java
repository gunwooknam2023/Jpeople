package com.gunwook.jpeople.alarm.repository;

import com.gunwook.jpeople.alarm.entity.Alarm;
import com.gunwook.jpeople.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUser(User user);
    List<Alarm> findByUserAndCheckFalse(User user);
}
