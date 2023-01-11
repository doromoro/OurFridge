package com.example.recipe2022.model.dto;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor

    public class UserRequestDto {

    @Getter
    @Setter
    public static class myPageDto {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class newPasswd {
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,15}$",
                message = "비밀번호는 10~15 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
        private String passWd;
        private String email;
    }
    @Getter
    @Setter
    public static class SignUp {

        @Email
        @NotBlank(message = "이메일을 입력해주세요")
        private String email;

        @NotBlank(message = "별명을 입력해주세요")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,30}$", message = "닉네임은 특수문자를 제외한 2자 이상이여야 합니다")
        private String name;

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,15}$",
                message = "비밀번호는 10~15 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
        private String password;

        private String date;

        private String gender;

        private String nums;

    }
    @Getter
    @Setter
    public static class Login {
        @Email
        @NotBlank(message = "이메일을 입력해주세요")
        private String email;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }
    @Getter
    @Setter
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Logout {
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;
    }
}