package com.example.recipe2022.service;


import com.example.recipe2022.config.jwt.JwtTokenProvider;
import com.example.recipe2022.exception.global.CustomException;
import com.example.recipe2022.model.data.User;
import com.example.recipe2022.model.dto.SignupRequest;
import com.example.recipe2022.model.enumer.ErrorCode;
import com.example.recipe2022.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImple implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    @Override
    public int signUp(SignupRequest requestDto) {

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            //throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
            throw new CustomException(ErrorCode.EMAIL_IS_EXIST);
        }

        User user = userRepository.save(requestDto.toEntity());
        user.encodePassword(passwordEncoder);

        //user.addUserAuthority();
        return user.getId();
    }
    @Override
    public String login(Map<String, String> members) {

        User member = userRepository.findByEmail(members.get("email"))
                //.orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

        String password = members.get("password");
        if (passwordEncoder.matches(passwordEncoder.encode(password), password )) {
            //throw new CustomException(ErrorCode.POSTS_NOT_FOUND);
            throw new CustomException(ErrorCode.PASSWD_NOT_FOUND);
        }

        List<String> roles = new ArrayList<>();
        roles.add(member.getRole().name());


        return tokenProvider.createToken(member.getName(), roles);
    }
}