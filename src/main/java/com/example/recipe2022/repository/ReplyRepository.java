package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Board;
import com.example.recipe2022.data.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findAllByBoard(Board board);

}