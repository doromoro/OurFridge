package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

    /*
    @Transactional  //로그인 시 마지막 로그인 일시 수정
    @Modifying(clearAutomatically = true)
    public void updateLastLogin(@Param("EMAIL_ADDR") String email, @Param("LAST_LOGIN_DATE") LocalDateTime last);


    @Transactional  //로그인 시 마지막 로그인 일시 수정
    @Modifying(clearAutomatically = true)
    public void passwordFail(@Param("PASSWD_INPUT_FAIL_COUNT") int count);
    */
}