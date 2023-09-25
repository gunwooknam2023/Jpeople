package com.gunwook.jpeople.post.dto;

import com.gunwook.jpeople.post.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    @NotEmpty(message = "제목을 입력하세요.")
    private String title;

    @NotEmpty(message = "내용을 입력하세요")
    private String contents;
}
