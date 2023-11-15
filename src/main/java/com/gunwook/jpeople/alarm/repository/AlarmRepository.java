package com.gunwook.jpeople.alarm.repository;

import com.gunwook.jpeople.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

}
