package com.example.recipe2022.view;

import com.example.recipe2022.config.redis.RedisUtils;
import com.example.recipe2022.model.dto.UserRequestDto;
import com.example.recipe2022.model.vo.MyPageVo;
import com.example.recipe2022.model.vo.Response;
import com.example.recipe2022.service.Helper;
import com.example.recipe2022.service.UsersService;
import com.example.recipe2022.service.interfacee.EmailService;
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
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "사용자 관련 기능")
public class UserController {

    private final UsersService usersService;
    private final EmailService emailService;
    private final Response response;
    private final RedisUtils redisUtils;

    @PostMapping("/mypage/pw-valid")
    @ApiOperation(value = "pw 리셋을 위한 인증 메일", notes= "")
    public ResponseEntity<?> pw(@RequestParam String email, @RequestParam String validatedCode) {
        return usersService.pw(email, validatedCode);
    }

    @GetMapping("/mypage/my-fridge")
    @ApiOperation(value = "마이 페이지 냉장고", notes= "마이 페이지 with jwtToken")
    public ResponseEntity<?> viewMyFridge(@ApiIgnore Authentication authentication) {
        return usersService.viewMyFridge(authentication);
    }

    @RequestMapping(value = "/mypage/passwd-reset", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> passwdReset(
            @ApiParam(value="사용자가 입력해야 하는 정보")
            @RequestParam UserRequestDto.newPasswd newPasswd,
            @ApiParam(value="인증 메일을 받을 이메일")
            MyPageVo.pwReset resetEmail) {
        return usersService.passwdReset(newPasswd, resetEmail);
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
            UserRequestDto.SignUp signUp,
            @RequestParam("validateCode")
            @ApiParam(value="사용자가 받은 인증 코드", required=true, example="6글자")
            String validateCode,
            @ApiIgnore Errors errors
    ) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        String email = redisUtils.getData(validateCode);
        return usersService.signUp(signUp, email);
    }
    @PostMapping("/mailSend")    //메일 인증 시작
    @ApiOperation(value = "인증 메일 발송", notes= "사용자가 인증 코드를 받을 이메일 주소를 입력 받고, 메일 전송")
    void mailSend(
            @RequestParam("email")
            @ApiParam(value = "인증 메일을 받을 이메일 주소", example = "~~~@~~~")
            String email) throws Exception {
        emailService.sendSimpleMessage(email);       //메일 전송
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "email&passwd 받아 로그인을 진행 -> JWT TOKEN(인증 토큰, 새로고침 토큰)을 반환")
    public ResponseEntity<?> login(@Validated UserRequestDto.Login login, @ApiIgnore Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return usersService.login(login);
    }

    @PostMapping("/reissue")
    @ApiOperation(value = "토큰 재발급", notes= "Refresh token을 재발급")
    public ResponseEntity<?> reissue(
            @Validated
            @ApiParam(value = "리프레시 토큰)")
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
            UserRequestDto.Logout logout, Errors errors) {
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