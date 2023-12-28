package com.gunwook.jpeople.mypage.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateNickname {

    @Size(max = 7)
    private String nickname;
}
