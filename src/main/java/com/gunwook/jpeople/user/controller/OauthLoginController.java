package com.gunwook.jpeople.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gunwook.jpeople.security.jwt.JwtUtil;
import com.gunwook.jpeople.user.dto.OauthTokenDto;
import com.gunwook.jpeople.user.service.GoogleService;
import com.gunwook.jpeople.user.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class OauthLoginController {
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtUtil jwtUtil;

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException{
        OauthTokenDto tokenDto = kakaoService.socialLogin(code);
        jwtUtil.addJwtToCookie(tokenDto.getAccessToken(), response);
        return "redirect:/";
    }

    @GetMapping("/user/google/callback")
    public String googleLogin(@RequestParam String code, HttpServletResponse response) throws  JsonProcessingException{
        OauthTokenDto tokenDto = googleService.socialLogin(code);
        jwtUtil.addJwtToCookie(tokenDto.getAccessToken(), response);
        return "redirect:/";
    }

}
