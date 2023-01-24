package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Board;
import com.example.recipe2022.data.entity.FavoriteBoard;
import com.example.recipe2022.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteBoardRepository extends JpaRepository<FavoriteBoard, Integer> {
    Optional<FavoriteBoard> findById(int id);

    FavoriteBoard findByBoardAndUser(Board board, Users user);

    FavoriteBoard findFavoriteByBoard(Board board);

}