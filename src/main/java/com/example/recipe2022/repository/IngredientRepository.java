package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findByIngredientNameContaining(String ingredientName);

    boolean existsByIngredientId(int id);
    boolean existsByIngredientName(String name);
    Optional<Ingredient> findByIngredientId(int id);
    Optional<Ingredient> findByIngredientName(String name);
}