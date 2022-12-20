package com.example.recipe2022.config;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 인증되지 않았을 경우(비로그인) AuthenticationEntryPoint 부분에서 AuthenticationException 발생시키면서
 * 비로그인 상태에서 인증실패 시, AuthenticationEntryPoint 로 핸들링되어 이곳에서 처리
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException, ServletException {
        // status를 401 에러로 지정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // json 리턴 및 한글깨짐 수정.
        response.setContentType("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = "잘못된 접근입니다";
        try {
            json.put("code", "9999");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            json.put("message", message);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        PrintWriter out = response.getWriter();
        out.print(json);
    }
}
