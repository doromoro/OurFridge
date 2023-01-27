package com.example.recipe2022.controller;

import com.example.recipe2022.data.dto.RecipeDto;
import com.example.recipe2022.data.entity.recipe;
import com.example.recipe2022.data.entity.Favoriterecipe;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.repository.RecipeRepository;
import com.example.recipe2022.service.recipeService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeController {
    private final RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private recipeService recipeService;

    /**
     * 레시피 메인
     */
    @GetMapping("/recipe")
    public ResponseEntity<?> mainrecipes(
//    public ModelAndView mainrecipes(
            Model model,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ){
        // 서브 메인 리스트
        Page<recipe> recipes = recipeService.findByUseYN('Y', pageable);
        log.debug("recipes :: [{}]", pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);

        return recipeService.mainrecipes(pageable);
    }

    /**
     * 검색
     */
    @GetMapping({"/recipe/search"})
    public ResponseEntity<?> searchrecipes(Model model,
                                          @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                          @RequestParam(required = false, defaultValue = "") String search
    ) {
        Page<recipe> recipes = recipeService.findByUseYNAndTitleContaining('Y', search, pageable);

        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);

        return recipeService.searchrecipes(pageable, search);
    }
    /**
     * 필터
     */
    @GetMapping({"/recipe/filter"})
    public ResponseEntity<?> filterrecipes(Model model,
                                          @PageableDefault(size = 5, sort = "recipeId", direction = Sort.Direction.DESC) Pageable pageable,
                                          @RequestParam(required = false, defaultValue = "") String filter
    ) {
        Page<Recipe> recipes = recipeService.findByUseYNAndFoodClassTypeCode('Y', filter, pageable);

        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);

        return recipeService.filterrecipes(pageable, filter);
    }

    /**
     * 조회수 높은 순
     */
    @GetMapping("/recipe/bestView")
    public ResponseEntity<?> bestViewrecipes(
            Model model,
            @PageableDefault(size = 5, sort = "view", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<recipe> recipes = recipeService.findByUseYN('Y', pageable);
        log.debug("recipes :: [{}]", pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);
        return recipeService.mainrecipes(pageable);
    }
    /**
     즐겨찾기 높은 순
     */
    @GetMapping("/recipe/bestFavorited")
    public ResponseEntity<?> bestFavoritedrecipes(
            Model model,
            @PageableDefault(size = 5, sort = "favorited", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<recipe> recipes = recipeService.findByUseYN('Y', pageable);
        log.debug("recipes :: [{}]", pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);
        return recipeService.mainrecipes(pageable);
    }
    /**
     * 즐겨찾기한 게시판
     */
    @GetMapping("/recipe/favorited")
    public ResponseEntity<?> favoritedrecipes(
            Model model,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @ApiIgnore Authentication authentication

    ){
        Page<Favoriterecipe> recipes = recipeService.findByUseYNAndUser('Y', authentication, pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);
        return recipeService.favoritedrecipes(pageable, authentication);
    }

    /**
     *  즐겨찾기하기, 즐겨찾기취소하기
     */
    @PostMapping("/recipe/registerFavorited")      //회원 가입 버튼
    @ApiOperation(value = "레시피 즐겨찾기")
    public ResponseEntity<?> favoritedRegisterRecipe(@ApiParam(value = "게시글 id", required = true)@ApiIgnore Authentication authentication, @RequestParam int recipeSeq) {
        log.info("레시피 즐겨찾기");
        return recipeService.favoritedRegisterRecipe(authentication, recipeSeq);
    }

    /**
     레시피 디테일
     */
    @GetMapping("/recipe/view-detail")
    public ResponseEntity<?> viewRecipeDetail(
            @ApiParam(value = "레시피 번호")
            int recipeSeq
    ){
        recipeService.updateCount(recipeSeq);
        return recipeService.viewRecipeDetail(recipeSeq);
    }
    @GetMapping("/recipe/view-ingredient-detail")
    public ResponseEntity<?> viewRecipeIngredientDetail(
            @ApiParam(value = "레시피 번호")
            int recipeSeq
    ){
        return recipeService.viewRecipeIngredientDetail(recipeSeq);
    }
    @GetMapping("/recipe/view-course-detail")
    public ResponseEntity<?> viewRecipeCourseDetail(
            @ApiParam(value = "레시피 번호")
            int recipeSeq
    ){
        return recipeService.viewRecipeCourseDetail(recipeSeq);
    }
    @GetMapping("/recipe/view-reply")
    public ResponseEntity<?> viewRecipeReply(
            @ApiParam(value = "레시피 번호")
            int recipeSeq
    ){
        return recipeService.viewRecipeReply(recipeSeq);
    }



    @GetMapping("/recipe/season")
    public String recipeSeason(){
        return "recipe/recipe-season";
    }


    //레시피 생성
    @PostMapping("/recipe-create")
    @ApiOperation(value = "레시피 등록")
    public ResponseEntity<?> save(@ApiIgnore Authentication authentication, RecipeDto.recipeCreate recipeDto) {
        log.info("레시피 등록");
        return recipeService.createRecipe(authentication, recipeDto);
    }
    @PostMapping(value = "/recipe/put-ingredient")
    @ApiOperation(value = "레시피에 재료 추가")
    public ResponseEntity<?> putIngredientToRecipe(
            @ApiParam(value = "재료 이름")
            int seq,
            @ApiParam(value = "레시피 고유 번호")
            int recipeSeq,
            RecipeDto.recipeIngredientCreate recipeIngredientDto) {
        log.info("레시피에 재료 추가");
        return recipeService.putIngredientToRecipe(seq, recipeSeq, recipeIngredientDto);
    }

    @PostMapping(value = "/recipe/put-course")
    @ApiOperation(value = "레시피에 요리 과정 추가")
    public ResponseEntity<?> putCourseToRecipe(
            @ApiParam(value = "레시피 고유 번호")
            int recipeSeq,
            RecipeDto.recipeCourseCreate recipeCourseDto){
        log.info("레시피에 요리 과정 추가");
        return recipeService.putCourseToRecipe(recipeSeq, recipeCourseDto);
    }

    @PostMapping(value = "/recipe-delete")       //회원 가입 버튼
    @ApiOperation(value = "레시피 삭제")
    public ResponseEntity<?> deleteRecipe(@RequestParam int recipeSeq) {
        log.info("레시피 삭제");
        return recipeService.deleteRecipe(recipeSeq);
    }

    @PostMapping(value = "/recipe/delete-ingredient")
    @ApiOperation(value = "n번 레시피에서 재료 삭제")
    public ResponseEntity<?> deleteIngredientToRecipe(
            @ApiParam(value = "재료 고유 번호")
            int ingSeq,
            @ApiParam(value = "삭제할 재료가 있는 레시피")
            int recipeSeq
    ){
        log.info("레시피에 재료 삭제");
        return recipeService.deleteIngredientToRecipe(ingSeq, recipeSeq);
    }

    @PostMapping(value = "/recipe/delete-course")
    @ApiOperation(value = "n번 레시피에서 과정 삭제")
    public ResponseEntity<?> deleteCourseToRecipe(
            @ApiParam(value = "레시피 과정 번호")
            int order,
            @ApiParam(value = "삭제할 과정이 있는 레시피")
            int recipeSeq
    ){
        log.info("레시피에 특정 과정 삭제");
        return recipeService.deleteCourseToRecipe(order, recipeSeq);
    }

    @PostMapping(value = "/recipe-update")       //회원 가입 버튼
    @ApiOperation(value = "레시피 수정")
    public ResponseEntity<?> updateRecipe(@RequestParam int recipeSeq, RecipeDto.recipeCreate recipeDto) {
        log.info("레시피 수정");
        return recipeService.updateRecipe(recipeSeq, recipeDto);
    }
    @PostMapping(value = "/recipe/update-ingredient")
    @ApiOperation(value = "n번 레시피에서 재료 수정")
    public ResponseEntity<?> updateRecipeIngredient(
            @ApiParam(value = "재료 고유 번호")
            int ingSeq,
            @ApiParam(value = "수정할 재료가 있는 레시피")
            int recipeSeq,
            RecipeDto.recipeIngredientCreate recipeIngredientDto
    )
    {
        log.info("레시피에 재료 수정");
        return recipeService.updateRecipeIngredient(ingSeq, recipeSeq, recipeIngredientDto);
    }

    @PostMapping(value = "/recipe/update-course")
    @ApiOperation(value = "n번 레시피에서 과정 수정")
    public ResponseEntity<?> updateCourseToRecipe(
            @ApiParam(value = "레시피 과정 번호")
            int order,
            @ApiParam(value = "수정할 과정이 있는 레시피")
            int recipeSeq,
            RecipeDto.recipeCourseCreate recipeCourseDto
    )
    {
        log.info("레시피에 과정 수정");
        return recipeService.updateCourseToRecipe(order, recipeSeq, recipeCourseDto);
    }
}