package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Optional<Board> findByBoardSeq(int id);
    @Modifying
    @Query("update Board p set p.viewCnt = p.viewCnt + 1 where p.boardSeq = :id")
    int updateCount(int id);

    Page<Board> findByUseYNAndTitleContaining(Character useYN, String title, Pageable pageable);

    Page<Board> findByUseYN(Character useYN, Pageable pageable);

    boolean existsByBoardSeq(int recipeSeq);

}