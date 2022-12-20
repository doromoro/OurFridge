package com.example.recipe2022.domain.member;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "user")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "USER_SEQ")
    private int userSeq;

    @Column (name = "LOGIN_ID")
    private String loginId;

    @Column(name = "EMAIL_ADDR")
    private String email;

    @Column (name = "LOGIN_PASSWD")
    private String password;

    @Column (name = "GENDER_CODE")
    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(int userSeq, String loginId, String email, String password, String gender, Role role) {
        this.userSeq = userSeq;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(memberFormDto.getLoginId())
                .email(memberFormDto.getEmail())
                .gender(memberFormDto.getGender())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))  //암호화처리
                .role(Role.USER)
                .build();
    }
}
