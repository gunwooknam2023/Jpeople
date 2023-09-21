package com.gunwook.jpeople.schedulePost.controller;

import com.gunwook.jpeople.schedulePost.dto.SchedulePostRequestDto;
import com.gunwook.jpeople.schedulePost.dto.SchedulePostResponseDto;
import com.gunwook.jpeople.schedulePost.service.SchedulePostService;
import com.gunwook.jpeople.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SchedulePostController {
    private final SchedulePostService schedulePostService;

    /**
     * 일정 추가
     * @param userDetails 유저 정보
     * @param schedulePostRequestDto 입력 정보
     * @return 일정 추가 성공/실패 여부
     */
    @PostMapping("/schedules")
    ResponseEntity<String> createSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody SchedulePostRequestDto schedulePostRequestDto){
        String result = schedulePostService.createSchedule(userDetails.getUser(), schedulePostRequestDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 일정 수정
     * @param userDetails 유저 정보
     * @param schedulePostRequestDto 입력 정보
     * @param schedule_id 수정할 일정 아이디
     * @return 일정 수정 성공/실패 여부
     */
    @PutMapping("/schedules/{schedule_id}")
    ResponseEntity<String> updateSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody SchedulePostRequestDto schedulePostRequestDto,
                                          @PathVariable Long schedule_id){
        String result = schedulePostService.updateSchedule(userDetails.getUser(), schedulePostRequestDto, schedule_id);
        return ResponseEntity.ok(result);
    }

    /**
     * 일정 삭제
     * @param userDetails 유저 정보
     * @param schedule_id 삭제할 일정 아이디
     * @return 일정 삭제 성공/실패 여부
     */
    @DeleteMapping("/schedules/{schedule_id}")
    ResponseEntity<String> deleteSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable Long schedule_id){
        String result = schedulePostService.deleteSchedule(userDetails.getUser(), schedule_id);
        return ResponseEntity.ok(result);
    }

    /**
     * 일정 전체 조회
     * @param userDetails 유저 정보
     * @return 일정 정보
     */
    @GetMapping("/schedules")
    ResponseEntity<List<SchedulePostResponseDto>> getSchedules(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<SchedulePostResponseDto> schedulePostResponseDtos = schedulePostService.getSchedules(userDetails.getUser());
        return ResponseEntity.ok(schedulePostResponseDtos);
    }

    /**
     * 일정 단건 조회
     * @param userDetails 유저 정보
     * @param schedule_id 조회할 일정 아이디
     * @return 일정 정보
     */
    @GetMapping("/schedules/{schedule_id}")
    ResponseEntity<SchedulePostResponseDto> getSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable Long schedule_id){
        SchedulePostResponseDto schedulePostResponseDto = schedulePostService.getSchedule(userDetails.getUser(), schedule_id);
        return ResponseEntity.ok(schedulePostResponseDto);
    }
}
