package com.gunwook.jpeople.post.dto;

import com.gunwook.jpeople.post.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    @Size(max = 30, message = "제목은 최대 30자까지 입력 가능합니다.")
    @NotEmpty(message = "제목을 입력하세요.")
    private String title;

    @NotEmpty(message = "내용을 입력하세요")
    private String contents;
}
