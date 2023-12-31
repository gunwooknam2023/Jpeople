package com.gunwook.jpeople.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message =  "패스워드를 입력해주세요.")
    private String password;

    @NotBlank(message =  "닉네임을 입력해주세요.")
    @Size(max=7)
    private String nickname;

    @NotBlank(message =  "자기소개를 입력해주세요.")
    @Size(max=20)
    private String introduction;

    private boolean admin;

    private String adminToken;

    public SignUpRequestDto(String username, String password, String nickname, String introduction){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
