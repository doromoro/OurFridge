package com.example.recipe2022.view;

import com.example.recipe2022.model.dto.SignupRequest;
import com.example.recipe2022.model.repository.UserRepository;
import com.example.recipe2022.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public int join(@Valid @RequestBody SignupRequest request) throws Exception {
        return userService.signUp(request);
    }
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        return userService.login(user);
    }
}