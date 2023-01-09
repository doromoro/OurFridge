package com.example.recipe2022.model.data;

import com.example.recipe2022.model.enumer.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_member")
@Data
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private int id;

    @Column(name = "username", nullable = false)
    private String name;

    @Column(name = "email_addr", nullable = false)
    private String email;

    @Column(name = "login_id")
    private String uid;

    @Column(name = "pic_file_id")
    private String picture;

    @Column(name = "login_passwd")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_DIV_CD", nullable = false)
    private Role role;

    @Column(name = "BDAY")
    private String date;

    @Column(name = "MOBILE_TEL_NUM")
    private String nums;


    @Builder
    public User(String email, String uid, String name, String picture, String date, String nums, Role role) {
        this.email = email;
        this.uid = uid;
        this.name = name;
        this.picture = picture;
        this.date = date;
        this.nums = nums;
        this.password = password;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
    }