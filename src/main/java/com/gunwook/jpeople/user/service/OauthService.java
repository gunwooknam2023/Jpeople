package com.gunwook.jpeople.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gunwook.jpeople.user.dto.OauthTokenDto;
import com.gunwook.jpeople.user.dto.OauthUserDto;
import com.gunwook.jpeople.user.entity.User;

import java.io.UnsupportedEncodingException;

public interface OauthService {

    OauthTokenDto socialLogin(String code) throws JsonProcessingException, UnsupportedEncodingException;


    String getToken(String code) throws JsonProcessingException, UnsupportedEncodingException;


    OauthUserDto getUserInfo(String accessToken) throws JsonProcessingException;

    User registerUserIfNeeded(OauthUserDto oauthTokenDto);

}
