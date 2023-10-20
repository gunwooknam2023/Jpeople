package com.gunwook.jpeople.schedulePost.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SchedulePostRequestDto {

    @Size(max = 10, message = "제목은 10자 이하여야 합니다.")
    private String title;

    @Size(max = 10, message = "내용은 10자 이하여야 합니다.")
    private String contents;
}
