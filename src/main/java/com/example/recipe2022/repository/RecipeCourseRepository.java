package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.data.entity.RecipeCourse;
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

    @Transactional
    void deleteAllByRecipe(Recipe recipe);


}