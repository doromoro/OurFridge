package com.example.recipe2022.handler.oauth;

import com.example.recipe2022.config.jwt.JwtTokenProvider;
import com.example.recipe2022.data.dao.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2CustomSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider tokenService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("성공!");
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        // 최초 로그인이라면 회원가입 처리를 한다.
        UserResponseDto.TokenInfo token = tokenService.generateToken(authentication);
        log.info("{}", token);
        redisTemplate.opsForValue()
                .set(authentication.getName(), token.getRefreshToken(), token.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, UserResponseDto.TokenInfo token)
            throws IOException {
        response.addHeader("Auth", token.getAccessToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");
    }

}