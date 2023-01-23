package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Ingredient;
import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.data.RecipeCourse;
import com.example.recipe2022.model.data.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeCourseRepository extends JpaRepository<RecipeCourse, Integer> {
    int countByRecipe(Recipe recipe);

    Optional<RecipeCourse> findByRecipeAndRecipeOrder(Recipe recipe, int order);
    List<RecipeCourse> findAllByRecipe(Recipe recipe);

    boolean existsByRecipeAndRecipeOrder(Recipe recipe, int order);

    RecipeCourse findByRecipeCourseSeq(int seq);
    @Transactional
    void deleteByRecipeCourseSeq(int id);





}