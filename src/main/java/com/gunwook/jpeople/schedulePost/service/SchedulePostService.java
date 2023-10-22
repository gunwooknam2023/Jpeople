package com.gunwook.jpeople.schedulePost.service;

import com.gunwook.jpeople.schedulePost.dto.SchedulePostRequestDto;
import com.gunwook.jpeople.schedulePost.dto.SchedulePostResponseDto;
import com.gunwook.jpeople.schedulePost.entity.SchedulePost;
import com.gunwook.jpeople.schedulePost.repository.SchedulePostRepository;
import com.gunwook.jpeople.schedulecard.entity.ScheduleCard;
import com.gunwook.jpeople.schedulecard.repository.ScheduleCardRepository;
import com.gunwook.jpeople.security.UserDetailsImpl;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulePostService {
    private final SchedulePostRepository schedulePostRepository;
    private final ScheduleCardRepository scheduleCardRepository;
    private final UserRepository userRepository;

    public String createSchedule(User user, SchedulePostRequestDto schedulePostRequestDto) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        SchedulePost schedulePost = new SchedulePost(schedulePostRequestDto, user);
        schedulePostRepository.save(schedulePost);
        return "일정이 생성 되었습니다.";
    }

    @Transactional
    public String updateSchedule(User user, SchedulePostRequestDto schedulePostRequestDto, Long scheduleId) {
        SchedulePost schedulePost = schedulePostRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("일정이 존재하지 않습니다.")
        );

        if(!user.getId().equals(schedulePost.getUser().getId())){
            throw new IllegalArgumentException("일정 작성자만 수정이 가능합니다.");
        }

        schedulePost.updateSchedulePost(schedulePostRequestDto);
        return "일정이 수정 되었습니다.";
    }

    @Transactional
    public String deleteSchedule(User user, Long scheduleId) {
        SchedulePost schedulePost = schedulePostRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalArgumentException("일정이 존재하지 않습니다.")
        );

        if(!user.getId().equals(schedulePost.getUser().getId())){
            throw new IllegalArgumentException("일정 작성자만 삭제가 가능합니다.");
        }

        schedulePostRepository.delete(schedulePost);
        return "일정이 삭제 되었습니다.";
    }

    public List<SchedulePostResponseDto> getSchedules(User user) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        List<SchedulePost> schedulePosts = schedulePostRepository.findByUserId(user.getId());
        List<SchedulePostResponseDto> schedulePostResponseDtos = new ArrayList<>();

        for(SchedulePost schedulePost : schedulePosts){
            SchedulePostResponseDto schedulePostResponseDto = new SchedulePostResponseDto(schedulePost);
            schedulePostResponseDtos.add(schedulePostResponseDto);
        }

        return schedulePostResponseDtos;
    }

    public SchedulePostResponseDto getSchedule(User user, Long scheduleId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        SchedulePost schedulePost = schedulePostRepository.findByUserIdAndId(user.getId(), scheduleId).orElseThrow(
                () -> new IllegalArgumentException("일정이 존재하지 않습니다.")
        );

        SchedulePostResponseDto schedulePostResponseDto = new SchedulePostResponseDto(schedulePost);
        return schedulePostResponseDto;
    }

    @Transactional
    public Double getPercent(User user, Long scheduleId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        SchedulePost schedulePost = schedulePostRepository.findByUserIdAndId(user.getId(), scheduleId).orElseThrow(
                () -> new IllegalArgumentException("일정이 존재하지 않습니다.")
        );

        int complete = 0;
        List<ScheduleCard> scheduleCardList =  scheduleCardRepository.findBySchedulePostId(scheduleId);
        for(ScheduleCard card : scheduleCardList){
            if(card.getHealth()){
                complete++;
            }
        }

        double result = (scheduleCardList.size() / complete) * 100;
        schedulePost.setPercent(result);
        return result;
    }
}
