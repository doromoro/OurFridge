package com.example.recipe2022.service;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.data.User;
import com.example.recipe2022.model.dto.board.BoardSaveRequestDto;
import com.example.recipe2022.model.repository.BoardRepository;
import com.example.recipe2022.model.repository.RecipeRepository;
import com.example.recipe2022.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

//    @Transactional
//    public void save(Board board, User user) {
//        board.setView(0);
//        board.setUser(user);
//        boardRepository.save(board);
//    }

    /**
     * 글작성 로직
     */
//    @Transactional
//    public void save(Board board) {
//        board.setView(0);
//        board.setRecommend(0);
//        board.setUse_yn("Y");
//        board.setDiv("R");
//        boardRepository.save(board);
//    }

    @Transactional
    public void save(BoardSaveRequestDto recipe, String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        Recipe recipes = new Recipe();
        recipes.setTitle(recipe.getTitle());
        recipes.setContents(recipe.getContents());
        recipes.setFile(recipe.getFile());
        recipes.setFoodCode(recipe.getFoodCode());
        recipes.setVolume(recipe.getVolume());
        recipes.setTime(recipe.getTime());
        recipes.setLevel(recipe.getLevel());
        //recipes.setUser(user);
        recipeRepository.save(recipes);
    }

    @Transactional
    public void saves(BoardSaveRequestDto recipe, String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        Recipe recipes = Recipe.builder()
                .title(recipe.getTitle())
                .build();

        recipeRepository.save(recipes);
    }



//    MyPageVo.myFridgeDetail detailList = MyPageVo.myFridgeDetail.builder()
//            .fridgeSeq(fridge.getFridgeId())
//            .fridgeName(fridge.getFridgeName())
//            .fridgeDetail(fridge.getFridgeDetail())
//            .fridgeFavorite(fridge.isFridgeFavorite())
//            .build();
//            data.add(detailList);

//    @Transactional
//    public int save(BoardSaveRequestDto boardSaveRequestDto, User user) {
//        return boardRepository.save(boardSaveRequestDto.toEntity()).getId();
//    }

//    public Page<Board> list(Pageable pageable){
//        return boardRepository.findAll(pageable);
//    }

    /**
     * 글목록 로직
     */
    @Transactional
    public Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(title, content, pageable);
    }

    /**
     * 글상세 로직
     */
    @Transactional
    public Board detail(int id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id가 없습니다. id=" + id));
    }

    /**
     * 글 조회수 로직
     */
    @Transactional
    public int updateCount(int id) {
        return boardRepository.updateCount(id);
    }

}
