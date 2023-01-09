package com.example.recipe2022.service;

import com.example.recipe2022.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(name).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

}