package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<Files, Integer> {
    Files findByFileSeq(int id);
//    Files findByFileSeq(String purpose);

}