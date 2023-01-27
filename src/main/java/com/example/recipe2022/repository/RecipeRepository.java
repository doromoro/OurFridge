package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findAll();
    Optional<Recipe> findByRecipeSeq(int id);

    boolean existsByRecipeSeq(int id);

    Page<Recipe> findByUseYNAndFoodClassTypeCode(Character useYN, String filter, Pageable pageable);

    @Modifying
    @Query("update Recipe p set p.viewCnt = p.viewCnt + 1 where p.recipeSeq = :id")
    int updateCount(int id);

    Page<Recipe> findByUseYNAndTitleContaining(Character useYN, String title, Pageable pageable);

    Page<Recipe> findByUseYN(Character useYN, Pageable pageable);

}