package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {

    Optional<RecipeIngredient> findByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);

    @Transactional
    void deleteByRecipeIngredientSeq(int id);
}