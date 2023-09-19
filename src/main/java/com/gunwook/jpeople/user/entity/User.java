package com.gunwook.jpeople.user.entity;

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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;



    public User(SignUpRequestDto signUpRequestDto, String password, UserRoleEnum role){
        this.username = signUpRequestDto.getUsername();
        this.password = password;
        this.nickname = signUpRequestDto.getNickname();
        this.introduction = signUpRequestDto.getIntroduction();
        this.role = role;

    }

}
