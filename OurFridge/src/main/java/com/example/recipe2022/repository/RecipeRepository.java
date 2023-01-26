package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findAll();
    Optional<Recipe> findByRecipeId(int id);

    boolean existsByRecipeId(int id);

    Page<Recipe> findByUseYNAndFoodClassTypeCode(Character useYN, String filter, Pageable pageable);

}