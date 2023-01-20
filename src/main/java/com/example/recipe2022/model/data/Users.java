package com.example.recipe2022.model.data;

import com.example.recipe2022.model.enumer.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_user")
public class Users extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SEQ")
    private int id;
    @Column(name = "USERNAME")
    private String name;

    @Column(name = "EMAIL_ADDR", nullable = false, unique = true    )
    private String email;

    @Column(name = "PIC_FILE_ID")
    private String picture;

    @Column(name = "LOGIN_PASSWD")
    private String password;

    @Column(name = "BDAY")
    private LocalDateTime date;

    @Column(name = "MOBILE_TEL_NUM")
    private String nums;

    @Column(name = "GENDER_CODE")
    private String gender;

    @Column(name = "USER_DIV_CD")
    private Role role;

    @Column(name = "PASSWD_DATE")
    private LocalDateTime passwdDate;

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLogin;

    @Column(name = "PASSWD_INPUT_FAIL_COUNT")
    private int passwdFailCount;


    @Column(name = "PREV_LOGIN_PASSWD")
    private String lastPassword;


    @Column
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_seq")
    @ToString.Exclude
    List<Fridge> fridges = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "USER_SEQ")
    @ToString.Exclude
    List<Recipe> recipes = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public Users (String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public Users update(String name, String picture)
    {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}