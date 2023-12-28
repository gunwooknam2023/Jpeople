package com.gunwook.jpeople.alarm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gunwook.jpeople.alarm.dto.AlarmResponseDto;
import com.gunwook.jpeople.alarm.entity.Alarm;
import com.gunwook.jpeople.alarm.repository.AlarmRepository;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public Long userid(User user) {
        return user.getId();
    }

    // 사용자 ID를 키로 가지는 SSEEmitter 저장소
    private final Map<Long, List<SseEmitter>> sseEmitterMap = new ConcurrentHashMap<>();

    public void addSseEmitter(Long userId, SseEmitter emitter) {
        sseEmitterMap.computeIfAbsent(userId, key -> new ArrayList<>()).add(emitter);
    }

    public void removeSseEmitter(Long userId, SseEmitter emitter) {
        List<SseEmitter> emitters = sseEmitterMap.get(userId);
        if(emitters != null){
            emitters.remove(emitter);
        }
    }

    @Async
    public void sendAlarmToUser(Long userId, Alarm alarm){
        List<SseEmitter> emitters = sseEmitterMap.get(userId);

        if(emitters != null){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String alarmJson;

            try{
                alarmJson = objectMapper.writeValueAsString(alarm);
            } catch (IOException e){
                throw new RuntimeException("알림을 직렬화 하는중 오류가 발생했습니다.",e);
            }

            // 제거할 emitters를 저장할 리스트 생성.
            List<SseEmitter> emittersToRemove = new ArrayList<>();

            for(SseEmitter emitter : emitters){
                try{
                    emitter.send(SseEmitter.event().data(alarmJson));
                } catch (IOException e){
                    emittersToRemove.add(emitter);
                    System.out.println("SSE 연결이 끊겼습니다.");
                }
            }

            emitters.removeAll(emittersToRemove);
        }
    }

    public List<AlarmResponseDto> getAlarms(User user) {
        userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 알림목록을 불러올 수 있습니다.")
        );

        List<Alarm> alarms = alarmRepository.findByUserId(user.getId());
        List<AlarmResponseDto> alarmResponseDtos = new ArrayList<>();

        for(Alarm alarm : alarms){
            AlarmResponseDto alarmResponseDto = new AlarmResponseDto(alarm);
            alarmResponseDtos.add(alarmResponseDto);
        }

        return alarmResponseDtos;
    }

    @Transactional
    public String checkUpdate(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 알림입니다.")
        );

        alarm.updateRead();
        alarmRepository.save(alarm);

        return "읽음 처리 되었습니다.";
    }

    @Transactional
    public String checkAllUpdate(User user) {
        List<Alarm> alarms = alarmRepository.findByUserIdAndReadFalse(user.getId());

        for(Alarm alarm : alarms){
            alarm.updateRead();
            alarmRepository.save(alarm);
        }

        return "모든 알림이 읽음 처리 되었습니다.";
    }
}
