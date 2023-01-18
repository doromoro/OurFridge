package com.example.recipe2022.service;

import com.example.recipe2022.model.data.*;
import com.example.recipe2022.model.dto.BoardSimpleDto;
import com.example.recipe2022.model.dto.RecipeDto;
import com.example.recipe2022.model.repository.*;
import com.example.recipe2022.model.vo.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecipeService {
    private final BoardRepository boardRepository;
    private final FavoriteBoardRepository favoriteBoardRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final Response response;
    private final LikeBoardRepository likeBoardRepository;

    /**
     * 글작성 로직
     */

    @Transactional
    public ResponseEntity<?> createRecipe(Authentication authentication, RecipeDto.recipeCreate recipeDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        Recipe recipes = Recipe.builder()
                .title(recipeDto.getRecipeTitle())
                .contents(recipeDto.getRecipeContents())
                .file(recipeDto.getRecipeFile())
                .foodCode(recipeDto.getRecipeFoodCode())
                .volume(recipeDto.getRecipeVolume())
                .time(recipeDto.getRecipeTime())
                .level(recipeDto.getRecipeLevel())
                .build();
        Board boards = Board.builder()
                .title(recipeDto.getRecipeTitle())
                .contents(recipeDto.getRecipeContents())
                .user(users)
                .div("R")
                .build();
        recipeRepository.save(recipes);
        boardRepository.save(boards);
        return response.success("레시피가 생성되었습니다!");
    }

    @Transactional
    public ResponseEntity<?> updateRecipe(int recipeSeq, RecipeDto.recipeCreate recipeDto){
        if(!recipeRepository.existsById(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(recipeSeq).orElseThrow();
        Board currentBoard = boardRepository.findById(recipeSeq).orElseThrow();
        String updateTitle = recipeDto.getRecipeTitle();
        String updateContents = recipeDto.getRecipeContents();
        String updateFiles = recipeDto.getRecipeFile();
        int updateFoodCode = recipeDto.getRecipeFoodCode();
        String updateTime = recipeDto.getRecipeTime();
        int updateLevel = recipeDto.getRecipeLevel();
        currentRecipe.setTitle(updateTitle);
        currentRecipe.setContents(updateContents);
        currentRecipe.setFile(updateFiles);
        currentRecipe.setFoodCode(updateFoodCode);
        currentRecipe.setTime(updateTime);
        currentRecipe.setLevel(updateLevel);
        currentBoard.setTitle(updateTitle);
        currentBoard.setContents(updateContents);
        recipeRepository.save(currentRecipe);
        boardRepository.save(currentBoard);
        return response.success("레시피 수정 성공 ");
    }

    @Transactional
    public ResponseEntity<?> deleteRecipe(int recipeSeq){
        if(!recipeRepository.existsById(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다. ", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(recipeSeq).orElseThrow();
        Board currentBoard = boardRepository.findById(recipeSeq).orElseThrow();
        currentRecipe.setUseYN(false);
        currentBoard.setUseYN(false);
//        recipeRepository.deleteById(recipeSeq);
//        boardRepository.deleteById(recipeSeq);
        return response.success("성공적으로 삭제되었습니다.");
    }

//    public ResponseEntity<?> defaultRecipe(int recipeSeq) {
//        Recipe currentRecipe = recipeRepository.findById(recipeSeq).orElseThrow();
//        if (!recipeRepository.existsById(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
//        currentRecipe.setRecipeFavorite(!currentRecipe.isRecipeFavorite());
//        log.info("즐겨찾기 레시피 수정 -> " + currentRecipe.isRecipeFavorite());
//        recipeRepository.save(currentRecipe);
//        return response.success("성공적으로 변경되었습니다.");
//    }

    @Transactional
    public ResponseEntity likeRecipe(Authentication authentication, int recipeSeq) {
        if(!recipeRepository.existsById(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Board currentBoard = boardRepository.findById(recipeSeq).orElseThrow();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();

        if(likeBoardRepository.findByBoardAndUser(currentBoard, users) == null) {
            // 좋아요를 누른적 없다면 LikeBoard 생성 후, 좋아요 처리
            currentBoard.setRecommend(currentBoard.getRecommend() + 1);
            LikeBoard likeBoard = new LikeBoard(currentBoard, users); // true 처리
            likeBoardRepository.save(likeBoard);
            return response.success("좋아요 처리 완료");
        } else {
            // 좋아요를 누른적 있다면 취소 처리 후 테이블 삭제
            LikeBoard likeBoard = likeBoardRepository.findByBoardAndUser(currentBoard, users);
            currentBoard.setRecommend(currentBoard.getRecommend() - 1);
            likeBoardRepository.delete(likeBoard);
            return response.success("좋아요 취소 완료");
        }
    }


    @Transactional
    public ResponseEntity favoriteRecipe(Authentication authentication, int recipeSeq) {
        if(!recipeRepository.existsById(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Board currentBoard = boardRepository.findById(recipeSeq).orElseThrow();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();

        if(favoriteBoardRepository.findByBoardAndUser(currentBoard, users) == null) {
            // 즐겨찾기를 누른적 없다면 Favorite 생성 후, 즐겨찾기 처리
            currentBoard.setFavorited(currentBoard.getFavorited() + 1);
            FavoriteBoard favorite = new FavoriteBoard(currentBoard, users); // true 처리
            favoriteBoardRepository.save(favorite);
            return response.success("즐겨찾기 처리 완료");
        } else {
            // 즐겨찾기 누른적 있다면 즐겨찾기 처리 후 테이블 삭제
            FavoriteBoard favorite = favoriteBoardRepository.findFavoriteByBoard(currentBoard);
            currentBoard.setFavorited(currentBoard.getFavorited() - 1);
            favoriteBoardRepository.delete(favorite);
            return response.success("즐겨찾기 취소 완료");
        }
    }


    @Transactional
    public List<BoardSimpleDto> bestBoards(Pageable pageable) {
        // 10 이상은 추천글
        Page<Board> boards = boardRepository.findByRecommendGreaterThanEqual(pageable, 10);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }

    /**
     * 글목록 로직
     */
    @Transactional
    public Page<Board> findByTitleContainingOrContentsContaining(String title, String contents, Pageable pageable) {
        return boardRepository.findByTitleContainingOrContentsContaining(title, contents, pageable);
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
