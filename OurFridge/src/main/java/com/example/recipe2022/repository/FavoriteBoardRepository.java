package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.recipe;
import com.example.recipe2022.data.entity.Favoriterecipe;
import com.example.recipe2022.data.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriterecipeRepository extends JpaRepository<Favoriterecipe, Integer> {
    Optional<Favoriterecipe> findById(int id);
    Page<Favoriterecipe> findByUseYNAndUser(Character useYN, Users user, Pageable pageable);
    Favoriterecipe findByrecipeAndUser(recipe recipe, Users user);

    List<Favoriterecipe> findAllByUseYNAndUser(Character useYN, Users user);

    Favoriterecipe findFavoriteByrecipe(recipe recipe);
}