package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}