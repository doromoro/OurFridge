package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Fridge;
import com.example.recipe2022.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Integer> {
    Optional<Fridge> findByFridgeId(int id);
    Optional<Fridge> findByUser(Users user);
    Long countByUser(Users user);
    boolean existsByFridgeId(int id);
    @Transactional
    void deleteByFridgeId(int id);
}