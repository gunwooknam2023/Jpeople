package com.gunwook.jpeople.schedulePost.repository;

import com.gunwook.jpeople.schedulePost.entity.SchedulePost;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SchedulePostRepository extends JpaRepository<SchedulePost, Long> {
    List<SchedulePost> findByUserId(Long userId);
    Optional<SchedulePost> findByUserIdAndId(Long userId, Long ScheduleId);
}
