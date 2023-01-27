package com.example.recipe2022.controller;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.RecipeDto;
import com.example.recipe2022.data.entity.FavoriteRecipe;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RecipeService recipeService;
    private final Response response;
    /**
     * 레시피 메인
     */
    @GetMapping("/recipe")
    public ResponseEntity<?> mainRecipes(
//    public ModelAndView mainRecipes(
            Model model,
            @PageableDefault(size = 5, sort = "recipeSeq", direction = Sort.Direction.DESC) Pageable pageable
    ){
        // 서브 메인 리스트
        Page<Recipe> recipes = recipeService.findByUseYN('Y', pageable);
        log.debug("recipes :: [{}]", pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);

//        ResponseEntity<?> list = recipeService.mainrecipes(pageable);
//        model.addAttribute("recipes", list.getBody());
//        return new ModelAndView("/recipe/recipe-main");
        return recipeService.mainRecipes(pageable);
    }

    /**
     * 검색
     */
    @GetMapping({"/recipe/search"})
    public ResponseEntity<?> searchRecipes(Model model,
                                           @PageableDefault(size = 5, sort = "recipeSeq", direction = Sort.Direction.DESC) Pageable pageable,
                                           @RequestParam(required = false, defaultValue = "") String search
    ) {
        Page<Recipe> recipes = recipeService.findByUseYNAndTitleContaining('Y', search, pageable);

        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);

        return recipeService.searchRecipes(pageable, search);
    }
    /**
     * 필터
     */
    @GetMapping({"/recipe/filter"})
    public ResponseEntity<?> filterRecipes(Model model,
                                           @PageableDefault(size = 5, sort = "recipeSeq", direction = Sort.Direction.DESC) Pageable pageable,
                                           @RequestParam(required = false, defaultValue = "") String filter
    ) {
        Page<Recipe> recipes = recipeService.findByUseYNAndFoodClassTypeCode('Y', filter, pageable);

        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);

        return recipeService.filterRecipes(pageable, filter);
    }

    /**
     * 조회수 높은 순
     */
    @GetMapping("/recipe/bestView")
    public ResponseEntity<?> bestViewRecipes(
            Model model,
            @PageableDefault(size = 5, sort = "viewCnt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<Recipe> recipes = recipeService.findByUseYN('Y', pageable);
        log.debug("recipes :: [{}]", pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);
        return recipeService.mainRecipes(pageable);
    }
    /**
     즐겨찾기 높은 순
     */
    @GetMapping("/recipe/bestFavorited")
    public ResponseEntity<?> bestFavoritedRecipes(
            Model model,
            @PageableDefault(size = 5, sort = "favorite", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<Recipe> recipes = recipeService.findByUseYN('Y', pageable);
        log.debug("recipes :: [{}]", pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);
        return recipeService.mainRecipes(pageable);
    }
    /**
     * 즐겨찾기한 게시판
     */
    @GetMapping("/recipe/favorited")
    public ResponseEntity<?> favoritedRecipes(
            Model model,
            @PageableDefault(size = 5, direction = Sort.Direction.DESC) Pageable pageable,
            @ApiIgnore Authentication authentication

    ){
        Page<FavoriteRecipe> recipes = recipeService.findByUseYNAndUser('Y', authentication, pageable);
        int startPage = Math.max(1, recipes.getPageable().getPageNumber() - 4);
        int endPage = Math.min(recipes.getTotalPages(), recipes.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("recipes", recipes);
        return recipeService.favoritedRecipes(pageable, authentication);
    }

    /**
     *  즐겨찾기하기, 즐겨찾기취소하기
     */
    @PostMapping("/recipe/register-favorite")      //회원 가입 버튼
    @ApiOperation(value = "레시피 즐겨찾기")
    public ResponseEntity<?> favoritedRegisterRecipe(@ApiParam(value = "게시글 id", required = true)@ApiIgnore Authentication authentication, @RequestBody RecipeDto.recipeFavoritedRegister recipeFavoritedRegisterDto) {
        log.info("레시피 즐겨찾기");
        return recipeService.favoritedRegisterRecipe(authentication, recipeFavoritedRegisterDto);
    }

    /**
     레시피 디테일
     */
    @GetMapping("/recipe/view-detail")
    public ResponseEntity<?> viewRecipeDetail(
            @RequestBody
            RecipeDto.recipeDetail recipeDetailDto
    ){
        recipeService.updateCount(recipeDetailDto.getRecipeSeq());
        return recipeService.viewRecipeDetail(recipeDetailDto);
    }
    @GetMapping("/recipe/view-ingredient-detail")
    public ResponseEntity<?> viewRecipeIngredientDetail(
            @RequestBody
            RecipeDto.recipeIngredientDetail recipeIngredientDetailDto
    ){
        return recipeService.viewRecipeIngredientDetail(recipeIngredientDetailDto);
    }
    @GetMapping("/recipe/view-course-detail")
    public ResponseEntity<?> viewRecipeCourseDetail(
            @RequestBody
            RecipeDto.recipeCourseDetail recipeCourseDetail
    ){
        return recipeService.viewRecipeCourseDetail(recipeCourseDetail);
    }
    @GetMapping("/recipe/view-reply")
    public ResponseEntity<?> viewRecipeReply(
            @RequestBody
            RecipeDto.recipeReply recipeReplyDto
    ){
        return recipeService.viewRecipeReply(recipeReplyDto);
    }



    @GetMapping("/recipe/season")
    public String recipeSeason(){
        return "recipe/recipe-season";
    }


    //레시피 생성
    @PostMapping("/recipe/create")
    @ApiOperation(value = "레시피 등록")
    public ResponseEntity<?> save(@ApiIgnore Authentication authentication
            , RecipeDto.recipeCreate recipeDto
            , RecipeDto.recipeIngredientCreate recipeIngredientCreate
            , RecipeDto.recipeCourseCreate recipeCourseCreate) {
        log.info("레시피 등록");
        recipeService.createRecipe(authentication, recipeDto);
        recipeService.putIngredientToRecipe(recipeIngredientCreate);    //아래2개가 createRecipe에 들어갈수있게끔
        recipeService.putCourseToRecipe(recipeCourseCreate);
        return response.success("성공");
    }
    @PostMapping("/recipe-create")
    @ApiOperation(value = "레시피 등록")
    public ResponseEntity<?> save(
            @ApiIgnore Authentication authentication
            , @RequestBody RecipeDto.recipeCreate recipeDto) {
        log.info("레시피 등록");
        return recipeService.createRecipe(authentication, recipeDto);
    }

    @PostMapping(value = "/recipe/put-ingredient")
    @ApiOperation(value = "레시피에 재료 추가")
    public ResponseEntity<?> putIngredientToRecipe(
            RecipeDto.recipeIngredientCreate recipeIngredientDto) {
        log.info("레시피에 재료 추가");
        return recipeService.putIngredientToRecipe(recipeIngredientDto);
    }

    @PostMapping(value = "/recipe/put-course")
    @ApiOperation(value = "레시피에 요리 과정 추가")
    public ResponseEntity<?> putCourseToRecipe(
            RecipeDto.recipeCourseCreate recipeCourseDto){
        log.info("레시피에 요리 과정 추가");
        return recipeService.putCourseToRecipe(recipeCourseDto);
    }

    @PostMapping(value = "/recipe-delete")       //회원 가입 버튼
    @ApiOperation(value = "레시피 삭제")
    public ResponseEntity<?> deleteRecipe(
            RecipeDto.recipeDelete recipeDeleteDto
    ) {
        log.info("레시피 삭제");
        return recipeService.deleteRecipe(recipeDeleteDto);
    }

    @PostMapping(value = "/recipe/delete-ingredient")
    @ApiOperation(value = "n번 레시피에서 재료 삭제")
    public ResponseEntity<?> deleteIngredientToRecipe(
            RecipeDto.recipeIngredientDelete recipeIngredientDeleteDto
    ){
        log.info("레시피에 재료 삭제");
        return recipeService.deleteIngredientToRecipe(recipeIngredientDeleteDto);
    }

    @PostMapping(value = "/recipe/delete-course")
    @ApiOperation(value = "n번 레시피에서 과정 삭제")
    public ResponseEntity<?> deleteCourseToRecipe(
            RecipeDto.recipeCourseDelete recipeCourseDeleteDto
    ){
        log.info("레시피에 특정 과정 삭제");
        return recipeService.deleteCourseToRecipe(recipeCourseDeleteDto);
    }

    @PostMapping(value = "/recipe-update")       //회원 가입 버튼
    @ApiOperation(value = "레시피 수정")
    public ResponseEntity<?> updateRecipe(RecipeDto.recipeUpdate recipeDto) {
        log.info("레시피 수정");
        return recipeService.updateRecipe(recipeDto);
    }
    @PostMapping(value = "/recipe/update-ingredient")
    @ApiOperation(value = "n번 레시피에서 재료 수정")
    public ResponseEntity<?> updateRecipeIngredient(
            RecipeDto.recipeIngredientUpdate recipeIngredientDto
    )
    {
        log.info("레시피에 재료 수정");
        return recipeService.updateRecipeIngredient(recipeIngredientDto);
    }

    @PostMapping(value = "/recipe/update-course")
    @ApiOperation(value = "n번 레시피에서 과정 수정")
    public ResponseEntity<?> updateCourseToRecipe(
            RecipeDto.recipeCourseUpdate recipeCourseDto
    )
    {
        log.info("레시피에 과정 수정");
        return recipeService.updateCourseToRecipe(recipeCourseDto);
    }
}