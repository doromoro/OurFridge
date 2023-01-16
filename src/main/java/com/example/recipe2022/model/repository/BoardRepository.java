package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Modifying
    @Query("update Board p set p.view = p.view + 1 where p.id = :id")
    int updateCount(int id);

    Page<Board> findByTitleContainingOrContentsContaining(String title, String contents, Pageable pageable);

}