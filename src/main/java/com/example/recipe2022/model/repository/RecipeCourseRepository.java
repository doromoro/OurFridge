package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.data.RecipeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCourseRepository extends JpaRepository<RecipeCourse, Integer> {
    int countByRecipe(Recipe recipe);

}