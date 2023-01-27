package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Board;
import com.example.recipe2022.data.entity.FavoriteBoard;
import com.example.recipe2022.data.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteBoardRepository extends JpaRepository<FavoriteBoard, Integer> {
    Optional<FavoriteBoard> findById(int id);
    Page<FavoriteBoard> findByUseYNAndUser(Character useYN, Users user, Pageable pageable);
    FavoriteBoard findByBoardAndUser(Board board, Users user);

    List<FavoriteBoard> findAllByUseYNAndUser(Character useYN, Users user);

    FavoriteBoard findFavoriteByBoard(Board board);
}