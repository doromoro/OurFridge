package com.example.recipe2022.model.repository;


import com.example.recipe2022.model.data.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>{

}
