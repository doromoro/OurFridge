package com.example.recipe2022.service;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.data.Users;
import com.example.recipe2022.model.dto.RecipeDto;
import com.example.recipe2022.model.repository.RecipeRepository;
import com.example.recipe2022.model.repository.UserRepository;
import com.example.recipe2022.model.vo.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    private final Response response;

    /**
     * 글작성 로직
     */

    @Transactional
    public ResponseEntity<?> createRecipe(Authentication authentication, RecipeDto.recipeCreate recipeDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        Recipe recipes = Recipe.builder()
                .recipeTitle(recipeDto.getRecipeTitle())
                .recipeContents(recipeDto.getRecipeContents())
                .recipeFile(recipeDto.getRecipeFile())
                .recipeFoodCode(recipeDto.getRecipeFoodCode())
                .recipeTime(recipeDto.getRecipeTime())
                .recipeLevel(recipeDto.getRecipeLevel())
                .user(users)
                .build();
        recipeRepository.save(recipes);
        return response.success("레시피가 생성되었습니다!");
    }

    public ResponseEntity<?> updateRecipe(int recipeSeq, RecipeDto.recipeCreate recipeDto){
        if(!recipeRepository.existsByRecipeId(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        String updateTitle = recipeDto.getRecipeTitle();
        String updateContents = recipeDto.getRecipeContents();
        String updateFiles = recipeDto.getRecipeFile();
        int updateFoodCode = recipeDto.getRecipeFoodCode();
        String updateTime = recipeDto.getRecipeTime();
        int updateLevel = recipeDto.getRecipeLevel();
        currentRecipe.setRecipeTitle(updateTitle);
        currentRecipe.setRecipeContents(updateContents);
        currentRecipe.setRecipeFile(updateFiles);
        currentRecipe.setRecipeFoodCode(updateFoodCode);
        currentRecipe.setRecipeTime(updateTime);
        currentRecipe.setRecipeLevel(updateLevel);
        recipeRepository.save(currentRecipe);
        return response.success("레시피 수정 성공 ");
    }

    public ResponseEntity<?> deleteRecipe(int recipeSeq){
        if(!recipeRepository.existsByRecipeId(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다. ", HttpStatus.BAD_REQUEST);
        }
        recipeRepository.deleteByRecipeId(recipeSeq);
        return response.success("성공적으로 삭제되었습니다.");
    }

    /**
     * 글목록 로직
     */
    @Transactional
    public Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable) {
        return recipeRepository.findByTitleContainingOrContentContaining(title, content, pageable);
    }

    /**
     * 글상세 로직
     */
    @Transactional
    public Recipe detail(int id) {
        return recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다. id=" + id));
    }

    /**
     * 글 조회수 로직
     */
    @Transactional
    public int updateCount(int id) {
        return recipeRepository.updateCount(id);
    }

}
