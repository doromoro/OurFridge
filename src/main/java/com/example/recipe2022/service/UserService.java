package com.example.recipe2022.service;

import com.example.recipe2022.model.dto.SignupRequest;

import java.util.Map;

public interface UserService {
    int signUp(SignupRequest requestDto) throws Exception;
    String login(Map<String, String> users);
}
