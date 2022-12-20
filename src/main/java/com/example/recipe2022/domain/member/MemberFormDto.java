package com.example.recipe2022.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class MemberFormDto {

    @NotBlank(message = "ID는 필수 입력 값입니다.")
    private String loginId;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "남, 여")
    private String gender;

    @Builder
    public MemberFormDto(String loginId, String email, String password, String gender) {
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
}
