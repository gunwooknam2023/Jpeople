package com.gunwook.jpeople.security;

import com.gunwook.jpeople.redis.RedisService;
import com.gunwook.jpeople.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        log.info("doFilterInternal");
        String AccessTokenValue = jwtUtil.getTokenFromRequest(request);
        String RefreshTokenValue = jwtUtil.getRefreshTokenFromRequest(request);

        // 로그인 페이지 요청 시 -> 토큰 모두 삭제
        if (request.getRequestURI().equals("/api/view/login")) {
            jwtUtil.deleteCookie(request, response);
        } else {
            // 엑세스 토큰 유효할 경우
            if (StringUtils.hasText(AccessTokenValue)) {
                AccessTokenValue = jwtUtil.substringToken(AccessTokenValue);
                if (jwtUtil.validateToken(AccessTokenValue)) {
                    Claims info = jwtUtil.getUserInfoFromToken(AccessTokenValue);
                    try {
                        setAuthentication(info.getSubject());
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return;
                    }
                }

            } else if(StringUtils.hasText(RefreshTokenValue)){
                // 엑세스 토큰은 유효하지 않으나, 리프레시 토큰이 존재할 경우
                log.error("엑세스 토큰이 없습니다. 하지마 리프레시 토큰은 존재합니다.");
                createNewAccessToken(request, response, RefreshTokenValue);
                return;
            } else{
                log.info("로그인이 안되어 있으면 403 에러가 발생할 수 있습니다.");
            }
        }

        filterChain.doFilter(request, response);
    }

    // 리프레시 토큰을 검증, 엑세스 토큰 재발급 메서드
    private void createNewAccessToken(HttpServletRequest request, HttpServletResponse response,
                                      String refreshTokenValue) throws IOException{
        refreshTokenValue = jwtUtil.substringToken(refreshTokenValue);
        redisService.generateAccessToken(request, response);
        response.sendRedirect(request.getRequestURI());
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
