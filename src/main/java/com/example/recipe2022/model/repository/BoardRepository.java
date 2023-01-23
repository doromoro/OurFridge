package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Optional<Board> findById(int id);
    @Modifying
    @Query("update Board p set p.view = p.view + 1 where p.id = :id")
    int updateCount(int id);

    Page<Board> findByUseYNAndTitleContaining(Character useYN, String title, Pageable pageable);

    Page<Board> findByUseYN(Character useYN, Pageable pageable);

    Page<Board> findByRecommendGreaterThanEqual(Pageable pageable, int number);

    boolean existsById(int recipeSeq);

}