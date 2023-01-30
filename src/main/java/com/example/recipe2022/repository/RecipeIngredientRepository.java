package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Ingredient;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.data.entity.RecipeIngredient;
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

    @Transactional
    void deleteAllByRecipe(Recipe recipe);

    RecipeIngredient findByRecipeIngredientSeq(int seq);

    boolean existsByIngredientAndRecipe(Ingredient ingredient, Recipe recipe);

    boolean existsByIngredient(Recipe recipe);
}