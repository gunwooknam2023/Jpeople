package com.gunwook.jpeople.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenDto {
    private String accessToken;
    private String refreshToken;
}
