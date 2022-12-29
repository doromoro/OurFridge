package com.example.recipe2022.view;

import com.example.recipe2022.config.redis.RedisUtils;
import com.example.recipe2022.model.dto.UserRequestDto;
import com.example.recipe2022.model.vo.Response;
import com.example.recipe2022.service.Helper;
import com.example.recipe2022.service.UsersService;
import com.example.recipe2022.service.interfacee.EmailService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class UserController {

    private final UsersService usersService;
    private final EmailService emailService;
    private final Response response;
    private final RedisUtils redisUtils;
    @PostMapping("/sign-up/confirm")        //회원 가입 버튼
    @ApiOperation(value = "회원 가입")
    public ResponseEntity<?> signUp(@Validated UserRequestDto.SignUp signUp, @RequestParam("ValidateCode") String ValidateCode, @ApiIgnore Errors errors) throws Exception {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        String email = redisUtils.getData(ValidateCode);        //사용자가 입력한 Vcode를 이용해 이메일을 찾고
        return usersService.signUp(signUp, email);
    }
    @PostMapping("/sign-up/mailSend")    //메일 인증 시작
    void mailSend(@RequestParam("email") String email) throws Exception {
        emailService.sendSimpleMessage(email);       //메일 전송
    }

    @PostMapping("/login")
    @ApiIgnore
    public ResponseEntity<?> login(@Validated UserRequestDto.Login login, @ApiIgnore Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.login(login);
    }

    @PostMapping("/reissue")
    @ApiIgnore
    public ResponseEntity<?> reissue(@Validated UserRequestDto.Reissue reissue, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.reissue(reissue);
    }

    @PostMapping("/logout")
    @ApiIgnore
    public ResponseEntity<?> logout(@Validated UserRequestDto.Logout logout, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.logout(logout);
    }

    @GetMapping("/authority")
    @ApiIgnore
    public ResponseEntity<?> authority() {
        log.info("ADD ROLE_ADMIN");
        return usersService.authority();
    }

    @GetMapping("/userTest")
    @ApiIgnore
    public ResponseEntity<?> userTest() {
        log.info("ROLE_USER TEST");
        return response.success();
    }

    @GetMapping("/adminTest")
    @ApiIgnore
    public ResponseEntity<?> adminTest() {
        log.info("ROLE_ADMIN TEST");
        return response.success();
    }
}