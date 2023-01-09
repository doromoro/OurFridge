package com.example.recipe2022.service;

import com.example.recipe2022.config.SecurityUtil;
import com.example.recipe2022.config.jwt.JwtTokenProvider;
import com.example.recipe2022.config.redis.RedisUtils;
import com.example.recipe2022.model.data.Users;
import com.example.recipe2022.model.dto.UserRequestDto;
import com.example.recipe2022.model.dto.UserResponseDto;
import com.example.recipe2022.model.enumer.Authority;
import com.example.recipe2022.model.repository.UserRepository;
import com.example.recipe2022.model.vo.MyPageVo;
import com.example.recipe2022.model.vo.Response;
import com.example.recipe2022.service.interfacee.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {

    private final UserRepository usersRepository;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;
    private final RedisUtils redisUtils;

    private final EmailService emailService;

    public ResponseEntity<?> pw (String email, String validatedCode) {
        String my = usersRepository.findByEmail(email).get().getUsername();         //내가 입력한 이메일을 기준으로 리포지토리에서 찾음
        if (!my.equals(email)) { return response.fail("없는 이메일입니다.", HttpStatus.BAD_REQUEST);}
        if (!redisUtils.getData(validatedCode).equals(my)) { return response.fail("인증 코드가 틀렸습니다", HttpStatus.BAD_REQUEST);}
        MyPageVo.pwReset resetEmail = MyPageVo.pwReset.builder()
                .email(email)
                .build();
        redisUtils.deleteData(email);
        return response.success(resetEmail, "인증 완료 -> 비밀번호 입력하고 바꿔라잉 ㅋㅋㅋ", HttpStatus.OK);
        // 새로운 URI에서 자원을 받아옴
    }

    public ResponseEntity<?> pwinput (@RequestParam UserRequestDto.newPasswd newPasswd, MyPageVo.pwReset resetEmail) {
        String email = resetEmail.getEmail();
        String oldPass = usersRepository.findByEmail(email).get().getPassword();
        String newPass = passwordEncoder.encode(newPasswd.getNewPasswd());
        if (newPass.equals(oldPass)) { return response.fail("이전 패스워드랑 같잖아요 .... 커뚜", HttpStatus.BAD_REQUEST); }
        Users user = Users.builder()
                .email(email)
                .password(newPass)
                .lastPassword(oldPass)
                .passwdFailCount(0)
                .build();
        usersRepository.save(user);
        return response.success("비밀번호 바꿈 수고링~~");
    }

    public ResponseEntity<?> viewMyPage(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        MyPageVo.myPage mypageVo = MyPageVo.myPage.builder()
                .userEmail(email)
                .userDate(usersRepository.findByEmail(email).get().getDate())
                .userNums(usersRepository.findByEmail(email).get().getNums())
                .userName(usersRepository.findByEmail(email).get().getName())
                .build();
        return response.success(mypageVo, "내 정보들이에용!!!", HttpStatus.OK);
    }
    public ResponseEntity<?> signUp(UserRequestDto.SignUp signUp, String validate) throws Exception {
        if (usersRepository.existsByEmail(signUp.getEmail())) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!(validate.equals(signUp.getEmail()))) {
            return response.fail("인증에 실패했습니다..", HttpStatus.BAD_REQUEST);
        }

        Users user = Users.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .date(now())
                .gender(signUp.getGender())
                .nums(signUp.getNums())
                .name(signUp.getName())
                .uid(signUp.getUid())
                .lastLogin(now())
                .passwdFailCount(0)
                .passwdDate(now())
                .lastPassword(passwordEncoder.encode(signUp.getPassword()))
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        usersRepository.save(user);
        redisUtils.deleteData(validate);    // 회원 가입이 정상적으로 진행 시, redis 토큰을 지움
        return response.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> login(UserRequestDto.Login login) {

        if (usersRepository.findByEmail(login.getEmail()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }   //아이디 자체가 틀렸을 경우

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set(authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(UserRequestDto.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get(authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set(authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(UserRequestDto.Logout logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get( authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete( authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    public ResponseEntity<?> authority() {
        // SecurityContext에 담겨 있는 authentication userEmail 정보
        String userEmail = SecurityUtil.getCurrentUserEmail();

        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        // add ROLE_ADMIN
        user.getRoles().add(Authority.ROLE_ADMIN.name());
        usersRepository.save(user);

        return response.success();
    }
}