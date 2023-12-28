package com.gunwook.jpeople.alarm.controller;

import com.gunwook.jpeople.alarm.dto.AlarmResponseDto;
import com.gunwook.jpeople.alarm.service.AlarmService;
import com.gunwook.jpeople.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping("/alarm/userid")
    public Long userid(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return alarmService.userid(userDetails.getUser());
    }

    /**
     * 알림을 받기 위한 SSE (Server-Sent Events) 생성
     * @param userId 알림을 받을 사용자
     * @return SSE 연결 객체
     */
    @GetMapping(value="/alarm/{userId}", produces = "text/event-stream")
    public SseEmitter getAlarm(@PathVariable Long userId){
        SseEmitter emitter = new SseEmitter();

        alarmService.addSseEmitter(userId, emitter);

        emitter.onTimeout(() -> alarmService.removeSseEmitter(userId, emitter));
        emitter.onCompletion(() -> alarmService.removeSseEmitter(userId, emitter));

        return emitter;
    }

    /**
     * 알림 목록 추출
     * @param userDetails 로그인된 사용자의 정보
     * @return 알림 목록 반환
     */
    @GetMapping("/alarm/list")
    public ResponseEntity<List<AlarmResponseDto>> getAlarms(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<AlarmResponseDto> alarmResponseDtos = alarmService.getAlarms(userDetails.getUser());
        return ResponseEntity.ok(alarmResponseDtos);
    }

    /**
     * 알림 단건 읽음 처리
     * @param alarmId 읽음 처리할 알림 번호
     * @return 처리 결과
     */
    @PostMapping("/alarm/read/{alarmId}")
    public ResponseEntity<String> checkUpdate(@PathVariable Long alarmId){
        String result = alarmService.checkUpdate(alarmId);
        return ResponseEntity.ok(result);
    }

    /**
     * 알림 전체 읽음 처리
     * @param userDetails 로그인 된 유저 정보
     * @return 처리 결과
     */
    @PostMapping("/alarm/allread")
    public ResponseEntity<String> checkAllUpdate(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String result = alarmService.checkAllUpdate(userDetails.getUser());
        return ResponseEntity.ok(result);
    }

}
