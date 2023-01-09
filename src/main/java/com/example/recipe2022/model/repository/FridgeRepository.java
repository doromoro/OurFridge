package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Fridge;
import com.example.recipe2022.model.data.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Integer> {
    Optional<Fridge> findByFridgeId(int id);
    Long countByUser(Users user);
    boolean existsByFridgeId(int id);
    @Transactional
    void deleteByFridgeId(int id);
}
