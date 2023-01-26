package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Codes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodesRepository extends JpaRepository<Codes, Integer> {
    List<Codes> findByCodeNmContaining(String nm);

    Codes findByCodeNm(String nm);
}