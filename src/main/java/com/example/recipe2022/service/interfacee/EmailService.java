package com.example.recipe2022.service.interfacee;

import com.example.recipe2022.data.dto.UserRequestDto;

public interface EmailService {
    void sendSimpleMessage(UserRequestDto.mailSend mailSend) throws Exception;
}