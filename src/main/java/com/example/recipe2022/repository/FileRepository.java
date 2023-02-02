package com.example.recipe2022.repository;

import com.example.recipe2022.data.entity.Files;
import com.example.recipe2022.data.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface FileRepository extends JpaRepository<Files, Integer> {
    Files findByFileSeq(int id);
//    Files findByFileSeq(String purpose);

//    @Transactional
//    void deleteAllByRecipe(List<Recipe> recipe);

}