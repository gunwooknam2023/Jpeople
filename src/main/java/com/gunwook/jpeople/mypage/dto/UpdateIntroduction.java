package com.gunwook.jpeople.mypage.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateIntroduction {

    @Size(max=20)
    private String introduction;
}
