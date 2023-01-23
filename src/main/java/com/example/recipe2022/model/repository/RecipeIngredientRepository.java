package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {

    Optional<RecipeIngredient> findByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);

    List<RecipeIngredient> findAllByRecipe(Recipe recipe);

    @Transactional
    void deleteByRecipeIngredientSeq(int id);

    RecipeIngredient findByRecipeIngredientSeq(int seq);

    boolean existsByIngredient(Ingredient ingredient);

    boolean existsByIngredient(int ingSeq);
}