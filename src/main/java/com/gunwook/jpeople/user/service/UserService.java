package com.gunwook.jpeople.user.service;


import com.gunwook.jpeople.redis.RefreshToken;
import com.gunwook.jpeople.redis.RefreshTokenRepository;
import com.gunwook.jpeople.security.jwt.JwtUtil;
import com.gunwook.jpeople.user.dto.SignUpRequestDto;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import com.gunwook.jpeople.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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


    public String logout(HttpServletRequest request, HttpServletResponse response, User user) {
//        // 세션 종료
//        HttpSession session = request.getSession(false);
//        if(session != null){
//            session.invalidate();
//        }
//
//        // 쿠키 삭제
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null){
//            for (Cookie cookie : cookies){
//                cookie.setMaxAge(0);
//                response.addCookie(cookie);
//            }
//        }
        Boolean result = refreshTokenRepository.delete(user.getUsername());
        if(!result){
            throw new IllegalArgumentException("리프레시 토큰을 삭제할 수 없습니다.");
        }
        jwtUtil.deleteCookie(request, response);
        return "로그아웃 성공";
    }

    // 엑세스 토큰 갱신 메서드
    public Boolean generateAccessToken(HttpServletRequest request, HttpServletResponse response){
        // 클라이언트 쿠키에서 refresh token 추출
        String InputRefreshToken = jwtUtil.getRefreshTokenFromRequest(request);
        String InputRefreshTokenValue = jwtUtil.substringToken(InputRefreshToken);

        // refresh token 이 없을 경우 예외 처리
        if(!StringUtils.hasText(InputRefreshToken)){
            log.error("리프레시 토큰이 존재하지 않습니다. 로그인 해주세요.");
            return false;
        }

        // refresh token 유효성 검사 불일치
        if(!jwtUtil.validateToken(InputRefreshTokenValue)){
            log.error("리프레시 토큰이 유효하지 않습니다.");
            jwtUtil.deleteCookie(request, response);
            return false;
        }

        // 유저 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(InputRefreshTokenValue);
        String username = claims.getSubject();
        UserRoleEnum role = jwtUtil.getUserRole(claims);

        // Redis 의 refresh token 일치 여부 판단
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username).get();
        if(InputRefreshToken.equals(refreshToken.getRefreshToken())){
            createAccessToken(response, username, role);
            return true;
        }
        return false;
    }

    // 엑세스 토큰 발급
    private void createAccessToken(HttpServletResponse response, String username, UserRoleEnum role){
        // 엑세스 토큰 발급, 쿠키에 저장
        String accessToken = jwtUtil.createToken(username, role);
        jwtUtil.addJwtToCookie(accessToken, response);
    }




}