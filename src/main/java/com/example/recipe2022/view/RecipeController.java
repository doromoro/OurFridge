package com.example.recipe2022.view;


import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.dto.RecipeDto;
import com.example.recipe2022.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping({"/recipe/main", "/recipe"})
    public String index(Model model,
                        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false, defaultValue = "") String search) {
        Page<Recipe> boards = recipeService.findByTitleContainingOrContentsContaining(search, search, pageable);
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


//
//    @GetMapping("/recipe/write")
//    public String recipeWrite() {
//        return "recipe/recipe-write";
//    }

    //레시피 생성
    @PostMapping("/recipe-create")
    @ApiOperation(value = "레시피 등록")
    public ResponseEntity<?> save(@ApiIgnore Authentication authentication, RecipeDto.recipeCreate recipeDto) {
        log.info("레시피 등록");
        return recipeService.createRecipe(authentication, recipeDto);
    }

    @PostMapping(value = "/recipe-delete")       //회원 가입 버튼
    @ApiOperation(value = "레시피 삭제")
    public ResponseEntity<?> deleteFridge(@RequestParam int recipeSeq) {
        log.info("레시피 삭제");
        return recipeService.deleteRecipe(recipeSeq);
    }

    @PostMapping(value = "/recipe-default")       //회원 가입 버튼
    @ApiOperation(value = "레시피 즐겨찾기")
    public ResponseEntity<?> defaultFridge(@RequestParam int recipeSeq) {
        log.info("레시피 즐겨찾기");
        return recipeService.defaultRecipe(recipeSeq);
    }

    @PostMapping(value = "/recipe-update")       //회원 가입 버튼
    @ApiOperation(value = "레시피 수정")
    public ResponseEntity<?> updateFridge(@RequestParam int recipeSeq, RecipeDto.recipeCreate recipeDto) {
        log.info("레시피 수정");
        return recipeService.updateRecipe(recipeSeq, recipeDto);
    }
}
