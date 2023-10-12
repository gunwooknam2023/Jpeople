package com.gunwook.jpeople.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String username; // email id 값 저장
    private String refreshToken;
}
