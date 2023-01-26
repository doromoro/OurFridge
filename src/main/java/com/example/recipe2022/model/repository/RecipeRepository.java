package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Optional<Recipe> findByRecipeId(int id);

    boolean existsByRecipeId(int id);

    Page<Recipe> findByUseYNAndFoodClassTypeCode(Character useYN, String filter, Pageable pageable);


//
//
//    @Transactional
//    void deleteById(int id);
//
//    @Modifying
//    @Query("update Board p set p.view = p.view + 1 where p.id = :id")
//    int updateCount(int id);
//
//    Page<Recipe> findByTitleContainingOrContentsContaining(String title, String contents, Pageable pageable);

}




