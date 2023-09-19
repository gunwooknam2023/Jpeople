package com.gunwook.jpeople.user.service;


import com.gunwook.jpeople.security.jwt.JwtUtil;
import com.gunwook.jpeople.user.dto.SignUpRequestDto;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${signup.admin.key}")
    private String ADMIN_TOKEN;

    public String signUp(SignUpRequestDto signUpRequestDto) {
        // 중복 체크
        if(userRepository.existsByUsername(signUpRequestDto.getUsername())){
            throw new IllegalArgumentException("이미 회원가입된 사용자 입니다.");
        }

        // 패스워드 암호화
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());

        // 권한 체크
        UserRoleEnum role = UserRoleEnum.USER;
        if(StringUtils.hasText(signUpRequestDto.getAdminToken())) {
            if (!ADMIN_TOKEN.equals(signUpRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀렸습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 유저 생성
        User user = new User(signUpRequestDto, password, role);
        userRepository.save(user);

        return "회원가입 성공";

    }


    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 세션 종료
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }

        // 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return "로그아웃 성공";
    }
}
