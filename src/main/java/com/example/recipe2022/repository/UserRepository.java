package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Users> findByGender(String name);

}