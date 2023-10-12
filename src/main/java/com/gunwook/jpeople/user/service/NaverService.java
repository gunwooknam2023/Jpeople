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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverService implements OauthService{
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${naver.client.id}")
    private String CLIENT_ID;

    @Value("${naver.client.secret}")
    private String CLIENT_SECRET;

    @Value("${naver.redirect.url}")
    private String REDIRECT_URL;

    @Override
    public OauthTokenDto socialLogin(String code) throws JsonProcessingException, UnsupportedEncodingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "네이버 사용자 정보" 가져오기
        OauthUserDto oauthUserDto = getUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User naverUser = registerUserIfNeeded(oauthUserDto);

        // 4. JWT 토큰 생성
        String createToken = jwtUtil.createToken(naverUser.getUsername(), naverUser.getRole());

        // 5. 리프레시 토큰 생성
        RefreshToken refreshToken = jwtUtil.createRefreshToken(naverUser.getUsername(), naverUser.getRole());
        refreshTokenRepository.save(refreshToken);

        return new OauthTokenDto(createToken, refreshToken.getRefreshToken());
    }

    @Override
    public String getToken(String code) throws JsonProcessingException, UnsupportedEncodingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://nid.naver.com")
                .path("/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("client_secret", CLIENT_SECRET)
                .queryParam("code", code)
                .queryParam("state", URLEncoder.encode("1016", "UTF-8")) // state: 임의 값 1016으로 설정
                .encode()
                .build()
                .toUri();

        StringBuilder responseBuilder = new StringBuilder();

        try {
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                responseBuilder.append(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = responseBuilder.toString();

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        return jsonNode.get("access_token").asText();
    }


    @Override
    public OauthUserDto getUserInfo(String accessToken) throws JsonProcessingException {
        String header = "Bearer " + accessToken; // Bearer 다음에 공백 추가

        String apiURL = "https://openapi.naver.com/v1/nid/me";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = getApiRequest(apiURL, requestHeaders);

        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        String id = jsonNode.get("response").get("id").asText();
        String nickname = jsonNode.get("response").get("nickname").asText();
        String email = jsonNode.get("response").get("email").asText();

        return new OauthUserDto(id, nickname, email);
    }

    private static String getApiRequest(String apiUrl, Map<String, String> requestHeaders) {
        // api 연결 확인
        HttpURLConnection connect = connecting(apiUrl);
        try {
            // get 메소드로 연결
            connect.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                connect.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = connect.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(connect.getInputStream());
            } else { // 에러 발생
                return readBody(connect.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            // 연결 해제
            connect.disconnect();
        }
    }

    private static HttpURLConnection connecting(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    @Override
    public User registerUserIfNeeded(OauthUserDto apiUserInfoDto) {
        // naverId 중복 확인
        String naverId = apiUserInfoDto.getId();
        User naverUser = userRepository.findByNaverId(naverId).orElse(null);

        if (naverUser == null) {
            // 네이버 사용자 email 동일한 email 가진 회원이 있는지 확인
            String naverEmail = apiUserInfoDto.getEmail();
            User sameEmailUser = userRepository.findByUsername(naverEmail).orElse(null);
            if (sameEmailUser != null) {
                naverUser = sameEmailUser;
                // 기존 회원정보에 네이버 Id 추가
                naverUser = naverUser.naverIdUpdate(naverId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                String email = apiUserInfoDto.getEmail();
                apiUserInfoDto.setNickname(email.substring(0,email.indexOf('@')));

                naverUser = new User(apiUserInfoDto, encodedPassword, UserRoleEnum.USER);
                naverUser.naverIdUpdate(naverId);
            }

            userRepository.save(naverUser);
        }

        return naverUser;
    }


}
