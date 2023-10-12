package com.gunwook.jpeople.redis;

import com.gunwook.jpeople.security.jwt.JwtUtil;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    /**
     * 엑세스 토큰 재발급 메서드
     * @param request 요청 Servlet
     * @param response 응답 Servlet
     */
    public Boolean generateAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // 클라이언트 쿠키에서 refresh token 추출
        String InputRefreshToken = jwtUtil.getRefreshTokenFromRequest(request);
        String InputRefreshTokenValue = jwtUtil.substringToken(InputRefreshToken);

        // refresh token 유효성 검사 불일치
        if (!jwtUtil.validateToken(InputRefreshTokenValue)) {
            log.error("Refresh Token does not valid.");
            jwtUtil.deleteCookie(request, response);
            return false;
        }

        // 유저 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(InputRefreshTokenValue);
        String username = claims.getSubject();
        UserRoleEnum role = jwtUtil.getUserRole(claims);

        // Redis 의 리프레시 토큰과 일치 여부 판단
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username).get();
        if (InputRefreshToken.equals(refreshToken.getRefreshToken())) {
            // 엑세스 토큰 생성
            createAccessToken(response, username, role);
            return true;
        }
        return false;
    }

    /**
     * 리프레시 토큰 삭제
     * @param request 요청 Servlet
     * @param response 응답 Servlet
     */
    public void deleteRefreshToken(HttpServletRequest request, HttpServletResponse response){
        // 클라이언트 쿠키에서 refresh token 추출
        String InputRefreshToken = jwtUtil.getRefreshTokenFromRequest(request);
        String InputRefreshTokenValue = jwtUtil.substringToken(InputRefreshToken);

        // 유저 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(InputRefreshTokenValue);
        String username = claims.getSubject();

        refreshTokenRepository.delete(username);
        jwtUtil.deleteCookie(request,response);
        response.setStatus(HttpStatus.OK.value());
    }

    private void createAccessToken(HttpServletResponse response, String username, UserRoleEnum role) {
        // access token 발급 및 쿠키에 저장
        String accessToken = jwtUtil.createToken(username, role);
        jwtUtil.addJwtToCookie(accessToken, response);
    }

}
