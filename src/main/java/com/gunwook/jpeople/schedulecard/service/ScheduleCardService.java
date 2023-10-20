package com.gunwook.jpeople.schedulecard.service;

import com.gunwook.jpeople.schedulePost.entity.SchedulePost;
import com.gunwook.jpeople.schedulePost.repository.SchedulePostRepository;
import com.gunwook.jpeople.schedulecard.dto.ScheduleCardRequestDto;
import com.gunwook.jpeople.schedulecard.dto.ScheduleCardResponseDto;
import com.gunwook.jpeople.schedulecard.entity.ScheduleCard;
import com.gunwook.jpeople.schedulecard.repository.ScheduleCardRepository;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleCardService {
    private final ScheduleCardRepository scheduleCardRepository;
    private final SchedulePostRepository schedulePostRepository;
    private final UserRepository userRepository;

    public String createSdCard(User user, ScheduleCardRequestDto scheduleCardRequestDto, Long schedulesId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        SchedulePost schedulePost = schedulePostRepository.findById(schedulesId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 일정 입니다.")
        );

        ScheduleCard scheduleCard = new ScheduleCard(scheduleCardRequestDto, user, schedulePost);
        scheduleCardRepository.save(scheduleCard);

        return "카드가 생성 되었습니다";
    }

    @Transactional
    public String updateSdCard(User user, ScheduleCardRequestDto scheduleCardRequestDto, Long cardId) {
         ScheduleCard scheduleCard = scheduleCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 입니다.")
         );

         if(!user.getId().equals(scheduleCard.getUser().getId())){
             throw new IllegalArgumentException("카드 작성자만 수정이 가능합니다.");
         }

        scheduleCard.updateCard(scheduleCardRequestDto);

        return "카드가 수정 되었습니다.";
    }

    public String deleteSdCard(User user, Long cardId) {
        ScheduleCard scheduleCard = scheduleCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 입니다.")
        );

        if(!user.getId().equals(scheduleCard.getUser().getId())){
            throw new IllegalArgumentException("카드 작성자만 삭제가 가능합니다.");
        }

        scheduleCardRepository.delete(scheduleCard);

        return "카드가 삭제 되었습니다.";
    }

    public List<ScheduleCardResponseDto> getCards(User user, Long scheduleId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        List<ScheduleCardResponseDto> scheduleCardResponseDtos = new ArrayList<>();
        List<ScheduleCard> scheduleCards = scheduleCardRepository.findBySchedulePostId(scheduleId);

        for(ScheduleCard scheduleCard : scheduleCards){
            ScheduleCardResponseDto scheduleCardResponseDto = new ScheduleCardResponseDto(scheduleCard);
            scheduleCardResponseDtos.add(scheduleCardResponseDto);
        }

        return scheduleCardResponseDtos;
    }

    public ScheduleCardResponseDto getCard(User user, Long cardId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        ScheduleCard scheduleCard = scheduleCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("카드가 존재하지 않습니다.")
        );

        ScheduleCardResponseDto scheduleCardResponseDto = new ScheduleCardResponseDto(scheduleCard);

        return scheduleCardResponseDto;
    }

    @Transactional
    public String healthTrue(User user, long cardId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        ScheduleCard scheduleCard = scheduleCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 입니다.")
        );

        scheduleCard.trueHealth();
        return "카드가 완료 처리 되었습니다.";
    }

    @Transactional
    public String healthFalse(User user, long cardId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        ScheduleCard scheduleCard = scheduleCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 입니다.")
        );

        scheduleCard.falseHealth();
        return "카드가 미완료 처리 되었습니다.";

    }


    public String health(User user, long cardId) {
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        ScheduleCard scheduleCard = scheduleCardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카드 입니다.")
        );

        if(scheduleCard.getHealth()){
            return "TRUE";
        } else{
            return "FALSE";
        }
    }
}
