package com.example.recipe2022.model.repository;


import com.example.recipe2022.model.data.FridgeIngredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Integer> {/*
    Optional<FridgeIngredient> findByFridgeId(int id);*/

}