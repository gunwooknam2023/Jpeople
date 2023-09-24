package com.gunwook.jpeople.schedulecard.controller;

import com.gunwook.jpeople.schedulecard.dto.ScheduleCardRequestDto;
import com.gunwook.jpeople.schedulecard.dto.ScheduleCardResponseDto;
import com.gunwook.jpeople.schedulecard.repository.ScheduleCardRepository;
import com.gunwook.jpeople.schedulecard.service.ScheduleCardService;
import com.gunwook.jpeople.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleCardController {
    private final ScheduleCardService scheduleCardService;

    /**
     * 카드 생성
     * @param userDetails 유저 정보
     * @param scheduleCardRequestDto 할일
     * @param schedule_id 포함될 일정
     * @return 카드 생성 성공/실패 여부
     */
    @PostMapping("/schedules/{schedule_id}/card")
    ResponseEntity<String> createSdCard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody ScheduleCardRequestDto scheduleCardRequestDto,
                                        @PathVariable Long schedule_id){
        String result = scheduleCardService.createSdCard(userDetails.getUser(), scheduleCardRequestDto, schedule_id);

        return ResponseEntity.ok(result);
    }

    /**
     * 카드 수정
     * @param userDetails 유저 정보
     * @param scheduleCardRequestDto 할일
     * @param card_id 수정할 카드 번호
     * @return 카드 수정 성공/실패 여부
     */
    @PutMapping("/schedules/card/{card_id}")
    ResponseEntity<String> updateSdCard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody ScheduleCardRequestDto scheduleCardRequestDto,
                                        @PathVariable Long card_id){
        String result = scheduleCardService.updateSdCard(userDetails.getUser(), scheduleCardRequestDto, card_id);

        return ResponseEntity.ok(result);
    }

    /**
     * 카드 삭제
     * @param userDetails 유저 정보
     * @param card_id 삭제할 카드 번호
     * @return 카드 삭제 성공/실패 여부
     */
    @DeleteMapping("/schedules/card/{card_id}")
    ResponseEntity<String> deleteSdCard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long card_id){
        String result = scheduleCardService.deleteSdCard(userDetails.getUser(), card_id);

        return ResponseEntity.ok(result);
    }

    /**
     * 카드 전체 조회
     * @param userDetails 유저 정보
     * @param schedule_id 조회할 일정 번호
     * @return 포함된 카드 전체 정보
     */
    @GetMapping("/schedules/cards/{schedule_id}")
    ResponseEntity<List<ScheduleCardResponseDto>> getCards(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long schedule_id){
        List<ScheduleCardResponseDto> scheduleCardResponseDtos = scheduleCardService.getCards(userDetails.getUser(), schedule_id);

        return ResponseEntity.ok(scheduleCardResponseDtos);
    }

    /**
     * 카드 단건 조회
     * @param userDetails 유저 정보
     * @param card_id 조회할 카드 번호
     * @return 포함된 카드 단건 정보
     */
    @GetMapping("/schedules/cards/{card_id}/one")
    ResponseEntity<ScheduleCardResponseDto> getCard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable Long card_id){
        ScheduleCardResponseDto scheduleCardResponseDto = scheduleCardService.getCard(userDetails.getUser(), card_id);

        return ResponseEntity.ok(scheduleCardResponseDto);
    }

    /**
     * 완료 상태 ON
     * @param userDetails 유저 정보
     * @param card_id 처리할 카드 번호
     * @return 완료 상태 처리 성공/실패 여부
     */
    @PostMapping("/schedules/card/health/t/{card_id}")
    ResponseEntity<String> healthTrue(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable long card_id){
        String result = scheduleCardService.healthTrue(userDetails.getUser(), card_id);

        return ResponseEntity.ok(result);
    }

    /**
     * 완료 상태 OFF
     * @param userDetails 유저 정보
     * @param card_id 처리할 카드 번호
     * @return 완료 상태 처리 성공/실패 여부
     */
    @PostMapping("/schedules/card/health/f/{card_id}")
    ResponseEntity<String> healthFalse(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable long card_id){
        String result = scheduleCardService.healthFalse(userDetails.getUser(), card_id);

        return ResponseEntity.ok(result);
    }






}
