package com.example.recipe2022.model.repository;


import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.data.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>{
    List<Reply> findAllByBoard(Board board);

}
