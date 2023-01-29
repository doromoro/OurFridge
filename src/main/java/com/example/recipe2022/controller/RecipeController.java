package com.example.recipe2022.controller;

import com.example.recipe2022.data.dao.RecipeVo;
import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.RecipeDto;
import com.example.recipe2022.data.entity.FavoriteRecipe;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.repository.RecipeRepository;
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

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeController {
    private final RecipeRepository recipeRepository;
    @Autowired
    private RecipeService recipeService;


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
    @PostMapping("/recipe/registerFavorited")      //회원 가입 버튼
    @ApiOperation(value = "레시피 즐겨찾기")
    public ResponseEntity<?> favoritedRegisterRecipe(@ApiParam(value = "게시글 id", required = true)@ApiIgnore Authentication authentication, RecipeDto.recipeFavoritedRegister recipeFavoritedRegisterDto) {
        log.info("레시피 즐겨찾기");
        return recipeService.favoritedRegisterRecipe(authentication, recipeFavoritedRegisterDto);
    }

    /**
     레시피 디테일
     */
    @ResponseBody
    @GetMapping("/recipe/view-detail")
    public ResponseEntity<?> viewRecipeDetail(
            @RequestBody
            RecipeDto.recipeDetail recipeDetailDto
    ){
        recipeService.updateCount(recipeDetailDto.getRecipeSeq());
//        return recipeService.viewRecipeDetail(recipeDetailDto);

        //레시피 게시글 상세 조회
        Map<String, Object> data = (Map<String, Object>) recipeService.viewRecipeDetail(recipeDetailDto).get("data");
        log.debug("data :: {}", data);

        if(!data.isEmpty()){
            RecipeVo.recipeDetail recipeDetail = (RecipeVo.recipeDetail) data.get("recipeDetail");
            log.debug("recipeDetail :: {}", recipeDetail);

            //레시피 재료 리스트 조회
            Map<String, Object> ingredient = (Map<String, Object>) recipeService.viewRecipeIngredientDetail(recipeDetailDto).get("data");
            List<RecipeVo.recipeIngredientDetail> ingredientList = (List<RecipeVo.recipeIngredientDetail>) ingredient.get("recipeIngredientList");
            log.debug("ingredientList :: {}", ingredientList);
            recipeDetail.setRecipeIngredientList(ingredientList);

            //레시피 과정 리스트 조회
            Map<String, Object> course = (Map<String, Object>) recipeService.viewRecipeCourseDetail(recipeDetailDto).get("data");
            List<RecipeVo.recipeCourseDetail> courseList = (List<RecipeVo.recipeCourseDetail>) course.get("recipeCourseList");
            log.debug("courseList :: {}", courseList);
            recipeDetail.setRecipeCourseList(courseList);

            //레시피 댓글 리스트 조회
            Map<String, Object> reply = (Map<String, Object>) recipeService.viewRecipeReply(recipeDetailDto).get("data");
            List<RecipeVo.recipeReply> replyList = (List<RecipeVo.recipeReply>) reply.get("replyList");
            log.debug("replyList :: {}", replyList);
            recipeDetail.setRecipeReplyList(replyList);

            return response.success(recipeDetail, "게시글 상세 조회에 성공하였습니다.", HttpStatus.OK);
        }else{
            return response.fail(data, "게시글 상세 조회에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }
//    @GetMapping("/recipe/view-ingredient-detail")
//    public ResponseEntity<?> viewRecipeIngredientDetail(
//            RecipeDto.recipeIngredientDetail recipeIngredientDetailDto
//    ){
//        return recipeService.viewRecipeIngredientDetail(recipeIngredientDetailDto);
//    }
//    @GetMapping("/recipe/view-course-detail")
//    public ResponseEntity<?> viewRecipeCourseDetail(
//            RecipeDto.recipeCourseDetail recipeCourseDetail
//    ){
//        return recipeService.viewRecipeCourseDetail(recipeCourseDetail);
//    }
//    @GetMapping("/recipe/view-reply")
//    public ResponseEntity<?> viewRecipeReply(
//            RecipeDto.recipeReply recipeReplyDto
//    ){
//        return recipeService.viewRecipeReply(recipeReplyDto);
//    }



    @GetMapping("/recipe/season")
    public String recipeSeason(){
        return "recipe/recipe-season";
    }


    //레시피 생성
    @ResponseBody
    @PostMapping("/recipe/create")
    @ApiOperation(value = "레시피 등록")
    public ResponseEntity<?> saves(Authentication authentication
            , @RequestBody RecipeDto.recipeCreate recipeDto
    ) {
        log.info("레시피 등록");

        // 레시피 create
        Map<String, Object> data = (Map<String, Object>) recipeService.createRecipe(authentication, recipeDto).get("data");
        if(!data.isEmpty()){
            int recipeSeq = (int) data.get("recipeSeq");
            log.info("recipeSeq : {} ", recipeSeq);

            // 레시피 재료 create
            List<RecipeDto.recipeIngredientCreate> recipeIngredientList = recipeDto.getRecipeIngredientList();
            log.info("recipeIngredientList : {} !!!!!!!!!!!!!! ", recipeIngredientList);

            for(RecipeDto.recipeIngredientCreate recipeIngredient : recipeIngredientList){
                recipeIngredient.setRecipeSeq(recipeSeq);
                recipeService.putIngredientToRecipe(recipeIngredient);
            }

            // 레시피 과정 create
            List<RecipeDto.recipeCourseCreate> recipeCourseList = recipeDto.getRecipeCourseList();
            for(RecipeDto.recipeCourseCreate recipeCourse : recipeCourseList){
                recipeCourse.setRecipeSeq(recipeSeq);
                recipeService.putCourseToRecipe(recipeCourse);
            }
        }
        return response.success("성공");
    }
//    @PostMapping("/recipe-create")
//    @ApiOperation(value = "레시피 등록")
//    public ResponseEntity<?> save(
//            @ApiIgnore Authentication authentication
//            , @RequestBody RecipeDto.recipeCreate recipeDto) {
//        log.info("레시피 등록");
//        return recipeService.createRecipe(authentication, recipeDto);
//    }

//    @PostMapping(value = "/recipe/put-ingredient")
//    @ApiOperation(value = "레시피에 재료 추가")
//    public ResponseEntity<?> putIngredientToRecipe(
//            RecipeDto.recipeIngredientCreate recipeIngredientDto) {
//        log.info("레시피에 재료 추가");
//        return recipeService.putIngredientToRecipe(recipeIngredientDto);
//    }
//
//    @PostMapping(value = "/recipe/put-course")
//    @ApiOperation(value = "레시피에 요리 과정 추가")
//    public ResponseEntity<?> putCourseToRecipe(
//            RecipeDto.recipeCourseCreate recipeCourseDto){
//        log.info("레시피에 요리 과정 추가");
//        return recipeService.putCourseToRecipe(recipeCourseDto);
//    }

    @PostMapping(value = "/recipe-delete")       //회원 가입 버튼
    @ApiOperation(value = "레시피 삭제")
    public ResponseEntity<?> deleteRecipe(
              Authentication authentication
            , @RequestBody RecipeDto.recipeDelete recipeDeleteDto
    ) {
        log.info("레시피 삭제");
        Map<String, Object> result = recipeService.deleteRecipe(authentication, recipeDeleteDto);
        //레시피
        if(result.get("code").equals(200)) {
//            int recipeSeq = (int) data.get("recipeSeq");
//            log.info("recipeSeq : {} ", recipeSeq);

            // 레시피 재료 create
//            List<RecipeDto.recipeIngredientDelete> recipeIngredientList = recipeDeleteDto.getRecipeIngredientList();
//            log.info("recipeIngredientList : {} !!!!!!!!!!!!!! ", recipeIngredientList);

//            for(RecipeDto.recipeIngredientDelete recipeIngredient : recipeIngredientList){
//                recipeIngredient.setRecipeSeq(recipeSeq);
//                recipeService.putIngredientToRecipe(recipeIngredient);
//            }

            // 레시피 과정 create
//            List<RecipeDto.recipeCourseDelete> recipeCourseList = recipeDeleteDto.getRecipeCourseList();
//            for(RecipeDto.recipeCourseCreate recipeCourse : recipeCourseList){
//                recipeCourse.setRecipeSeq(recipeSeq);
//                recipeService.putCourseToRecipe(recipeCourse);
//            }

        }
        return response.success("삭제 완료");
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