package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.FavoriteBoard;
import com.example.recipe2022.model.data.LikeBoard;
import com.example.recipe2022.model.data.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteBoardRepository extends JpaRepository<FavoriteBoard, Integer> {
    Optional<FavoriteBoard> findById(int id);

    FavoriteBoard findByBoardAndUser(Board board, Users user);

    FavoriteBoard findFavoriteByBoard(Board board);
}
