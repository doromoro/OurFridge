package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Fridge;
import com.example.recipe2022.data.entity.FridgeIngredient;
import com.example.recipe2022.data.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
    public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Integer> {
    Optional<FridgeIngredient> findByFridge(Fridge fridge);

    boolean existsByFridgeDetailSeq(int seq);
    Optional<FridgeIngredient> findByFridgeAndIngredient(Fridge fridge, Ingredient ingredient);
    List<FridgeIngredient> findAllByFridge(Fridge fridge);
    @Transactional
    void deleteByFridgeDetailSeq(int id);
}