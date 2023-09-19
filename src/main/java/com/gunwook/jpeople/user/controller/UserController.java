package com.gunwook.jpeople.user.controller;


import com.gunwook.jpeople.user.dto.SignUpRequestDto;
import com.gunwook.jpeople.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     * @param signUpRequestDto
     * @return 회원가입 성공/실패 여부
     */
    @PostMapping("/signup")
    ResponseEntity<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        String result = userService.signUp(signUpRequestDto);
        return ResponseEntity.ok(result);
    }

    /**
     * 로그아웃
     * @param request 요청 servlet
     * @param response 응답 servlet
     * @return 로그아웃 성공/실패 여부
     */
    @DeleteMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        String result = userService.logout(request, response);
        return ResponseEntity.ok(result);
    }
}
