package com.gunwook.jpeople.mypage.dto;

import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String username;
    private String nickname;
    private String introduction;
    private UserRoleEnum role;

    public ProfileResponseDto(User user){
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.role = user.getRole();
    }
}
