package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {



}
