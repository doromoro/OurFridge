package com.example.recipe2022.service;

import org.springframework.data.jpa.repository.JpaRepository;

public class Common {
    public static void saveIfNullEmail(String email, JpaRepository repository, Object entity) {
        if(email == null) {
            repository.save(entity);
        }
    }
}
