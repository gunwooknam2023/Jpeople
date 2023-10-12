package com.gunwook.jpeople.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunwook.jpeople.redis.RefreshToken;
import com.gunwook.jpeople.redis.RefreshTokenRepository;
import com.gunwook.jpeople.security.jwt.JwtUtil;
import com.gunwook.jpeople.user.dto.LoginRequestDto;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String AccessTokenValue = jwtUtil.getTokenFromRequest(request);
        String RefreshTokenValue = jwtUtil.getRefreshTokenFromRequest(request);

        if (StringUtils.hasText(AccessTokenValue) || StringUtils.hasText(RefreshTokenValue)) {
            jwtUtil.deleteCookie(request,response);
        }

        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            //log.info(requestDto.getUsername() + requestDto.getPassword());
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        // 엑세스 토큰 발급, 쿠키에 저장
        String accessToken = jwtUtil.createToken(username, role);
        jwtUtil.addJwtToCookie(accessToken, response);

        // 리프레시 토큰 발급, Redis에 저장
        RefreshToken refreshToken = jwtUtil.createRefreshToken(username, role);
        refreshTokenRepository.save(refreshToken);
        jwtUtil.addJwtToCookieRefreshToken(refreshToken.getRefreshToken(), response);

        log.info("로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("로그인 실패");
        response.setStatus(401);
    }

}