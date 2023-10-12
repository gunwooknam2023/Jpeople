package com.gunwook.jpeople.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gunwook.jpeople.redis.RefreshToken;
import com.gunwook.jpeople.redis.RefreshTokenRepository;
import com.gunwook.jpeople.security.jwt.JwtUtil;
import com.gunwook.jpeople.user.dto.OauthTokenDto;
import com.gunwook.jpeople.user.dto.OauthUserDto;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import com.gunwook.jpeople.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoService implements OauthService{
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client.id}")
    private String CLIENT_ID;

    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;

    @Override
    public OauthTokenDto socialLogin(String code) throws JsonProcessingException{
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        OauthUserDto oauthUserDto = getUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerUserIfNeeded(oauthUserDto);

        // 4. JWT 토큰 생성
        String createToken = jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getRole());

        // 5. 리프레시 토큰 생성
        RefreshToken refreshToken = jwtUtil.createRefreshToken(kakaoUser.getUsername(), kakaoUser.getRole());
        refreshTokenRepository.save(refreshToken);

        return new OauthTokenDto(createToken, refreshToken.getRefreshToken());
    }

    @Override
    public String getToken(String code) throws JsonProcessingException{
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID); // 자신의 REST API 키
        body.add("redirect_uri", REDIRECT_URL);
        body.add("code",code); // 인가 코드

        // 방법 1
        ResponseEntity<String> response = restTemplate.postForEntity(uri,new HttpEntity<>(body, headers),String.class);


        // HTTP 응답 (JSON) -> 액세스 토큰 값을 반환합니다.
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    @Override
    public OauthUserDto getUserInfo(String accessToken) throws  JsonProcessingException{
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        // Http 요청 보내기
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new OauthUserDto(id, nickname, email);
    }

    @Override
    public User registerUserIfNeeded(OauthUserDto userDto){
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String kakaoId = userDto.getId();
        User kakaoUser = (User) userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 과 동일한 id 를 가진 회원이 있는지 확인
            String kakaoEmail = userDto.getEmail();
            User sameEmailUser = userRepository.findByUsername(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                kakaoUser = new User(userDto, encodedPassword, UserRoleEnum.USER);
                kakaoUser.kakaoIdUpdate(kakaoId);
            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

}
