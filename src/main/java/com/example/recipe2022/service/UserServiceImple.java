package com.example.recipe2022.service;


import com.example.recipe2022.config.jwt.JwtTokenProvider;
import com.example.recipe2022.model.data.User;
import com.example.recipe2022.model.dto.SignupRequest;
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
    public int signUp(SignupRequest requestDto) throws Exception {

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        User user = userRepository.save(requestDto.toEntity());
        user.encodePassword(passwordEncoder);

        //user.addUserAuthority();
        return user.getId();
    }
    @Override
    public String login(Map<String, String> members) {

        User member = userRepository.findByEmail(members.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Email 입니다."));

        String password = members.get("password");
        String encodedPassword = passwordEncoder.encode(password);
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        List<String> roles = new ArrayList<>();
        roles.add(member.getRole().name());


        return tokenProvider.createToken(member.getName(), roles);
    }
}