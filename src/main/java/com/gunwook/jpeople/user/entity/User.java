package com.gunwook.jpeople.user.entity;

import com.gunwook.jpeople.user.dto.OauthUserDto;
import com.gunwook.jpeople.user.dto.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_url")
    private String profileUrl = "/images/logo.png";

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "naver_id")
    private String naverId;

    @Column(name = "google_id")
    private String googleId;



    public User(SignUpRequestDto signUpRequestDto, String password, UserRoleEnum role){
        this.username = signUpRequestDto.getUsername();
        this.password = password;
        this.nickname = signUpRequestDto.getNickname();
        this.introduction = signUpRequestDto.getIntroduction();
        this.role = role;
    }

    public User(OauthUserDto userDto, String password, UserRoleEnum role){
        this.username = userDto.getEmail();
        this.password = password;
        this.nickname = userDto.getNickname();
        this.introduction = "-";
        this.role = role;
    }

    public User kakaoIdUpdate(String kakaoId){
        this.kakaoId = kakaoId;
        return this;
    }

    public User googleIdUpdate(String googleId){
        this.googleId = googleId;
        return this;
    }

    public User naverIdUpdate(String naverId){
        this.naverId = naverId;
        return this;
    }

    public void setProfileUrl(String profileUrl){
        this.profileUrl = profileUrl;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }
}
