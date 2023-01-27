package com.example.recipe2022.service;

import com.example.recipe2022.config.SecurityUtil;
import com.example.recipe2022.config.jwt.JwtTokenProvider;
import com.example.recipe2022.config.redis.RedisUtils;
import com.example.recipe2022.data.dao.MyPageVo;
import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dao.UserResponseDto;
import com.example.recipe2022.data.dto.FileDto;
import com.example.recipe2022.data.dto.UserRequestDto;
import com.example.recipe2022.data.entity.Board;
import com.example.recipe2022.data.entity.Files;
import com.example.recipe2022.data.entity.Fridge;
import com.example.recipe2022.data.entity.Users;
import com.example.recipe2022.data.enumer.Authority;
import com.example.recipe2022.data.enumer.FilePurpose;
import com.example.recipe2022.data.enumer.Role;
import com.example.recipe2022.handler.general.FilesHandler;
import com.example.recipe2022.repository.FileRepository;
import com.example.recipe2022.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDateTime.now;

@Slf4j
@RequiredArgsConstructor
@Service
@RestControllerAdvice
public class UsersService {

    private final UserRepository usersRepository;
    private final Response response;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisUtils redisUtils;
    private final FilesHandler fileHandler;
    private final FileRepository fileRepository;

    public ResponseEntity<?> pw (UserRequestDto.validateEmail validate) {              //passwd 찾기 인증 컴포넌트
        //내가 입력한 이메일을 기준으로 리포지토리에서 찾음
        if (!usersRepository.existsByEmail(validate.getEmail())) {
            return response.fail("없는 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        String my = usersRepository.findByEmail(validate.getEmail()).get().getUsername();
        if (!redisUtils.getData(validate.getValidateCode()).equals(my)) { return response.fail("인증 코드가 틀렸습니다.", HttpStatus.BAD_REQUEST);}
        MyPageVo.pwReset resetEmail = MyPageVo.pwReset.builder()
                .email(validate.getEmail())
                .build();
        redisUtils.deleteData(validate.getEmail());
        return response.success(resetEmail, "인증에 성공했습니다", HttpStatus.OK);
    }

    public ResponseEntity<?> passwdReset (UserRequestDto.newPasswd newPasswd) {
        if (!usersRepository.existsByEmail(newPasswd.getEmail())) {
            return response.fail("없는 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        String email = newPasswd.getEmail();
        Users users = usersRepository.findByEmail(email).orElseThrow();
        String oldPass = users.getPassword();
        String newPass = passwordEncoder.encode(newPasswd.getPassWd());

        if (newPass.equals(oldPass)) { return response.fail("이전에 입력한 비밀번호와 같습니다 다른 비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST); }

        users.setPassword(newPass);
        users.setLastPassword(oldPass);
        users.setPasswdFailCount(0);
        return response.success("비밀번호를 변경했습니다.");
    }
    public ResponseEntity<?> viewMyFridge(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = usersRepository.findByEmail(email).orElseThrow();
        List<Fridge> fridgeList = usersRepository.findById(users.getUserSeq()).get().getFridges();
        log.info(email + "님의 정보입니다." + "현재 냉장고 개수는 " + fridgeList.size() + "개 입니다.");
        List<MyPageVo.myFridgeDetail> data =new ArrayList<>();
        for (Fridge fridge : fridgeList) {
            MyPageVo.myFridgeDetail detailList = MyPageVo.myFridgeDetail.builder()
                    .fridgeSeq(fridge.getFridgeId())
                    .fridgeName(fridge.getFridgeName())
                    .fridgeDetail(fridge.getFridgeDetail())
                    .fridgeFavorite(fridge.isFridgeFavorite())
                    .build();
            data.add(detailList);
        }
        return response.success(data,"냉장고 조회", HttpStatus.OK);
    }



    public ResponseEntity<?> viewMyPage(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        MyPageVo.myPage myPageVo = MyPageVo.myPage.builder()
                .userEmail(email)
                .userDate(usersRepository.findByEmail(email).get().getDate())
                .userNums(usersRepository.findByEmail(email).get().getNums())
                .userName(usersRepository.findByEmail(email).get().getName())
                .build();
        return response.success(myPageVo, "내 정보들이에용!!!", HttpStatus.OK);
    }
    public ResponseEntity<?> signUp(UserRequestDto.SignUp signUp) {
        if (usersRepository.existsByEmail(signUp.getEmail())) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!(signUp.getValidatedCode().equals(signUp.getEmail()))) {
            return response.fail("인증에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
        Users user = Users.builder()
                .email(signUp.getEmail())
                .gender(signUp.getGender())
                .nums(signUp.getNums())
                .name(signUp.getName())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .date(now())
                .lastLogin(now())
                .passwdFailCount(0)
                .passwdDate(now())
                .lastPassword(passwordEncoder.encode(signUp.getPassword()))
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .role(Role.USER)
                .build();
        usersRepository.save(user);
        redisUtils.deleteData(signUp.getValidatedCode());    // 회원 가입이 정상적으로 진행 시, redis 토큰을 지움
        return response.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> login(UserRequestDto.Login login, HttpServletResponse resp) {
        if (usersRepository.findByEmail(login.getEmail()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
        if (authenticationToken.toString() == null) { return response.fail("비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);}
        Authentication authentication =
                    authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        redisTemplate.opsForValue()
                .set(authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenInfo.getRefreshToken())
                .maxAge(7L * 24L * 60L * 60L)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());                                      //리프레시 토큰 in the cookie
        resp.addHeader("Authorization", "Bearer " + tokenInfo.getAccessToken());       //액세스 토큰
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
        // SecurityContext 담겨 있는 authentication userEmail 정보
        String userEmail = SecurityUtil.getCurrentUserEmail();

        Users user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        // add ROLE_ADMIN
        user.getRoles().add(Authority.ROLE_ADMIN.name());
        usersRepository.save(user);

        return response.success();
    }
    public ResponseEntity<?> updateUser(
            Authentication authentication,
            @RequestParam("update") String update,
            @RequestPart("files") MultipartFile files) throws Exception {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users user = usersRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new SimpleModule());
        FileDto.updateUserPicture abc = objectMapper.readValue(update, new TypeReference<>() {});
        log.info(abc.getDate() + "/" + abc.getUsername() + "/" + abc.getNums() + "/");
        log.info(files.getContentType() + "/" + files.getOriginalFilename() + "/" + files.getSize() + "/");
        Files picture = fileHandler.parseFileInfo(FilePurpose.USER_PICTURE, files);

        if (files.isEmpty())
        {
            return response.fail("파일을 안올렸네 ;;", HttpStatus.BAD_REQUEST);
        }
        else {
            fileRepository.save(picture);
        }
        user.setName(abc.getUsername());
        user.setNums(abc.getNums());
        user.setFiles(picture);
        usersRepository.save(user);
        return response.success(("사진 파일의 이름은 " + picture.getOriginalFile() + "사용자가 수정한 정보는 " + update));
    }
}