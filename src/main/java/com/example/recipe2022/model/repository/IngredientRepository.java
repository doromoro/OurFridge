package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByIngredientNameContaining(String ingredientName);
    Optional<Ingredient> findByIngredientName(String ingredientName);
    boolean existsByIngredientName(String ingredientName);
    boolean existsByIngredientId(int id);
    Optional<Ingredient> findByIngredientId(int id);
}