package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface recipeRepository extends JpaRepository<recipe, Integer> {

    Optional<recipe> findById(int id);
    @Modifying
    @Query("update recipe p set p.viewCnt = p.viewCnt + 1 where p.recipeSeq = :id")
    int updateCount(int id);

    Page<recipe> findByUseYNAndTitleContaining(Character useYN, String title, Pageable pageable);

    Page<recipe> findByUseYN(Character useYN, Pageable pageable);

    boolean existsById(int recipeSeq);

}