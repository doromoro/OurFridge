package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.LikeBoard;
import com.example.recipe2022.model.data.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Integer> {

    LikeBoard findByBoardAndUser(Board board, Users user);
}