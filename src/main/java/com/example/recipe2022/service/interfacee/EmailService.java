package com.example.recipe2022.service.interfacee;

import com.example.recipe2022.data.dto.UserRequestDto;
import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<?> sendSimpleMessage(UserRequestDto.mailSend mailSend) throws Exception;
    ResponseEntity<?> sendSimpleMessage(String a) throws Exception;
}