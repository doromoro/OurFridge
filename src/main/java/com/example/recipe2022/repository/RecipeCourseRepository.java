package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.data.entity.RecipeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCourseRepository extends JpaRepository<RecipeCourse, Integer> {
    int countByRecipe(Recipe recipe);

}