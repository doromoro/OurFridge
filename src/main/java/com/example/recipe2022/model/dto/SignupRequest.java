package com.example.recipe2022.model.dto;

import com.example.recipe2022.model.data.User;
import com.example.recipe2022.model.enumer.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
    public class SignupRequest {

    @Email
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "아이디를 입력해주세요")
    private String uid;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message = "닉네임이 너무 짧습니다.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
            message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    private String checkedPassword;

    private Role role;

    @Builder
    public User toEntity(){
        return User.builder()
                .email(email)
                .uid(uid)
                .name(name)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
