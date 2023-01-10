package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

}
