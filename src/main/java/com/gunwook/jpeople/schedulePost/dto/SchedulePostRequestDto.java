package com.gunwook.jpeople.schedulePost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SchedulePostRequestDto {

    @NotEmpty(message = "제목을 입력하세요.")
    @Size(max = 10, message = "제목은 10자 이하여야 합니다.")
    private String title;

    @NotEmpty(message = "내용을 입력하세요.")
    @Size(max = 10, message = "내용은 10자 이하여야 합니다.")
    private String contents;
}
