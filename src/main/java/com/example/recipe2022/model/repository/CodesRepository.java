package com.example.recipe2022.model.repository;

import com.example.recipe2022.model.data.Codes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodesRepository extends JpaRepository<Codes, Integer> {
    List<Codes> findByCodeNmContaining(String nm);

    Codes findByCodeNm(String nm);
}
