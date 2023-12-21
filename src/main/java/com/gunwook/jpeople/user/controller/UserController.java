package com.gunwook.jpeople.user.controller;


import com.gunwook.jpeople.schedulePost.dto.ScheduleManagerResponseDto;
import com.gunwook.jpeople.security.UserDetailsImpl;
import com.gunwook.jpeople.user.dto.SignUpRequestDto;
import com.gunwook.jpeople.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 유저의 전체, 달, 일별 달성도 조회
     * @param userDetails 로그인 정보
     * @return 달성도 정보
     */
    @GetMapping("/scheduleManager")
    ResponseEntity<ScheduleManagerResponseDto> scheduleManager(@AuthenticationPrincipal UserDetailsImpl userDetails){
        ScheduleManagerResponseDto scheduleManagerResponseDto = userService.scheduleManager(userDetails.getUser());
        return ResponseEntity.ok(scheduleManagerResponseDto);
    }

    /**
     * 회원가입
     * @param signUpRequestDto
     * @return 회원가입 성공/실패 여부
     */
    @PostMapping("/signup")
    ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
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
    ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        String result = userService.logout(request, response, userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    /**
     * 만료 전 access token 재발급 메서드
     * @param request 요청 Servlet
     * @param response 응답 Servlet
     * @return http status code 와 토큰 재발급 성공 여부
     */
    @GetMapping("/refresh/access-token")
    public ResponseEntity<String> generateRefreshToken(HttpServletRequest request, HttpServletResponse response){
        boolean result = userService.generateAccessToken(request, response);

        if(result){
            return ResponseEntity.ok("엑세스 토큰 생성 성공");
        } else{
            return ResponseEntity.badRequest().body("엑세스 토큰 생성 실패");
        }
    }
}
