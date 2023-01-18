package com.example.recipe2022.handler.oauth;

import com.example.recipe2022.config.jwt.JwtTokenProvider;
import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dao.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    private final Response resultResponse;
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
    public ResponseEntity<?> writeTokenResponse(HttpServletResponse response, UserResponseDto.TokenInfo token)
            throws IOException {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge(7L * 24L * 60L * 60L)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        return resultResponse.success(token, "반환", HttpStatus.OK);
    }
}