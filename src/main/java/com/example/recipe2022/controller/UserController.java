package com.example.recipe2022.controller;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.UserRequestDto;
import com.example.recipe2022.service.Helper;
import com.example.recipe2022.service.UsersService;
import com.example.recipe2022.service.interfacee.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "사용자 관련 기능")
public class UserController {

    private final UsersService usersService;
    private final EmailService emailService;
    private final Response response;

    @PostMapping("/testman")
    public ResponseEntity<?> updateUser(
            Authentication authentication,
            @RequestParam("update") String update,
            @RequestPart("files") MultipartFile files) throws Exception {
        return usersService.updateUser(authentication, update, files);
    }


    @GetMapping("/mypage/my-fridge")
    @ApiOperation(value = "마이 페이지 냉장고", notes= "마이 페이지 with jwtToken")
    public ResponseEntity<?> viewMyFridge(@ApiIgnore Authentication authentication) {
        return usersService.viewMyFridge(authentication);
    }

    @RequestMapping(value = "/mypage/passwd-reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> passwdReset(
            @ApiParam(value="인증 메일을 받을 이메일")
            @RequestBody UserRequestDto.newPasswd newPasswd) {
        return usersService.passwdReset(newPasswd);
    }
    @GetMapping("/mypage/my-info")
    @ApiOperation(value = "마이 페이지", notes= "마이 페이지 with jwtToken")
    public ResponseEntity<?> viewMyPage(@ApiIgnore Authentication authentication) {
        return usersService.viewMyPage(authentication);
    }
    @PostMapping("/sign-up")        //회원 가입 버튼
    @ApiOperation(value = "회원 가입", notes= "회원 가입이 실제로 이루어지는 공간")
    public ResponseEntity<?> signUp(
            @Validated
            @ApiParam(value="사용자가 입력해야 하는 정보", example="6글자")
            @RequestBody
            UserRequestDto.SignUp signUp,
            @ApiIgnore Errors errors
    ) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.signUp(signUp);
    }
    @PostMapping("/mailSend")    //메일 인증 시작
    @ApiOperation(value = "인증 메일 발송", notes= "사용자가 인증 코드를 받을 이메일 주소를 입력 받고, 메일 전송")
    void mailSend(
            @ApiParam(value = "인증 메일을 받을 이메일 주소", example = "~~~@~~~")
            @RequestBody
            String email) throws Exception {
        emailService.sendSimpleMessage(email);       //메일 전송
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "email&passwd 받아 로그인을 진행 -> JWT TOKEN(인증 토큰, 새로고침 토큰)을 반환")
    public ResponseEntity<?> login(@Validated @RequestBody UserRequestDto.Login login, @ApiIgnore Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.login(login, resp);
    }

    @PostMapping("/reissue")
    @ApiOperation(value = "토큰 재발급", notes= "Refresh token 재발급")
    public ResponseEntity<?> reissue(
            @Validated
            @ApiParam(value = "리프레시 토큰)")
            @RequestBody
            UserRequestDto.Reissue reissue,
            @ApiIgnore
            Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.reissue(reissue);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes= "로그아웃 with Token")
    public ResponseEntity<?> logout(
            @Validated
            @ApiParam(value = "jwt token")
            @RequestBody
            UserRequestDto.Logout logout,
            @ApiIgnore
            Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.logout(logout);
    }

    @GetMapping("/authority")
    @ApiOperation(value = "권한 추가", notes= "어드민 권한을 추가")
    public ResponseEntity<?> authority() {
        log.info("ADD ROLE_ADMIN");
        return usersService.authority();
    }
}