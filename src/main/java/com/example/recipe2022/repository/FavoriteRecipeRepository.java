package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.FavoriteRecipe;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.data.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipe, Integer> {
    Optional<FavoriteRecipe> findById(int id);
    Page<FavoriteRecipe> findByUseYNAndUser(Character useYN, Users user, Pageable pageable);
    FavoriteRecipe findByRecipeAndUser(Recipe recipe, Users user);

    List<FavoriteRecipe> findAllByUseYNAndUser(Character useYN, Users user);

    FavoriteRecipe findFavoriteByRecipe(Recipe recipe);
}