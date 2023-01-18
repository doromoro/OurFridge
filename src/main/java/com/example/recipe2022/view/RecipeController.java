package com.example.recipe2022.view;


import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.dto.RecipeDto;
import com.example.recipe2022.model.repository.RecipeRepository;
import com.example.recipe2022.model.vo.Response;
import com.example.recipe2022.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeController {
    private final RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @GetMapping({"/recipe/main", "/recipe"})
    public String index(Model model,
                        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false, defaultValue = "") String search) {
        Page<Board> boards = recipeService.findByTitleContainingOrContentsContaining(search, search, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "recipe/recipe-main";
    }

    @GetMapping("/recipe/{id}")
    public String findById(@PathVariable int id, Model model){
        model.addAttribute("board", recipeService.detail(id));
        recipeService.updateCount(id);
        return "recipe/recipe-detail";
    }


    @GetMapping("/recipe/season")
    public String recipeSeason(){
        return "recipe/recipe-season";
    }

    @GetMapping("/recipe/ranking")
    public String recipeRanking(){
        return "recipe/recipe-ranking";
    }


    //레시피 생성
    @PostMapping("/recipe-create")
    @ApiOperation(value = "레시피 등록")
    public ResponseEntity<?> save(@ApiIgnore Authentication authentication, RecipeDto.recipeCreate recipeDto) {
        log.info("레시피 등록");
        return recipeService.createRecipe(authentication, recipeDto);
    }

    @PostMapping(value = "/recipe-delete")       //회원 가입 버튼
    @ApiOperation(value = "레시피 삭제")
    public ResponseEntity<?> deleteRecipe(@RequestParam int recipeSeq) {
        log.info("레시피 삭제");
        return recipeService.deleteRecipe(recipeSeq);
    }

    @PostMapping("/recipe/{id}/favorites")      //회원 가입 버튼
    @ApiOperation(value = "레시피 즐겨찾기")
    public ResponseEntity<?> favoriteRecipe(@ApiParam(value = "레시피 id", required = true)@ApiIgnore Authentication authentication, @RequestParam int recipeSeq) {
        log.info("레시피 즐겨찾기");
        return recipeService.favoriteRecipe(authentication, recipeSeq);
    }

    @ApiOperation(value = "레시피 좋아요", notes = "사용자가 레시피 좋아요를 누릅니다.")
    @PostMapping("/recipe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> likeRecipe(@ApiParam(value = "게시글 id", required = true) @ApiIgnore Authentication authentication, @RequestParam int recipeSeq) {
        log.info("레시피 추천");
        return recipeService.likeRecipe(authentication, recipeSeq);
    }

    @ApiOperation(value = "인기글 조회", notes = "추천수 10이상 게시글을 조회합니다.")
    @GetMapping("/recipe/best")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> bestBoards(@PageableDefault(size = 5, sort = "liked", direction = Sort.Direction.DESC) Pageable pageable) {
        return (ResponseEntity<?>) recipeService.bestBoards(pageable);
    }

    @PostMapping(value = "/recipe-update")       //회원 가입 버튼
    @ApiOperation(value = "레시피 수정")
    public ResponseEntity<?> updateRecipe(@RequestParam int recipeSeq, RecipeDto.recipeCreate recipeDto) {
        log.info("레시피 수정");
        return recipeService.updateRecipe(recipeSeq, recipeDto);
    }
}
