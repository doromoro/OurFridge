package com.example.recipe2022.controller;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.BoardDto;
import com.example.recipe2022.data.dto.RecipeDto;
import com.example.recipe2022.data.entity.Board;
import com.example.recipe2022.data.entity.FavoriteBoard;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.repository.RecipeRepository;
import com.example.recipe2022.service.BoardService;
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
    private BoardService boardService;

    private final Response response;

    /**
     * 레시피 메인
     */
    @GetMapping("/recipe")
    public ResponseEntity<?> mainBoards(
//    public ModelAndView mainBoards(
            Model model,
            @PageableDefault(size = 5, sort = "boardSeq", direction = Sort.Direction.DESC) Pageable pageable
    ){
        // 서브 메인 리스트
        Page<Board> boards = boardService.findByUseYN('Y', pageable);
        log.debug("boards :: [{}]", pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);

//        ResponseEntity<?> list = boardService.mainBoards(pageable);
//        model.addAttribute("boards", list.getBody());

        // 서브 메인 필터 리스트
        // t_code

//        return new ModelAndView("/recipe/recipe-main");
        return boardService.mainBoards(pageable);
    }

    /**
     * 검색
     */
    @GetMapping({"/recipe/search"})
    public ResponseEntity<?> searchBoards(Model model,
                                          @PageableDefault(size = 5, sort = "boardSeq", direction = Sort.Direction.DESC) Pageable pageable,
                                          @RequestParam(required = false, defaultValue = "") String search
    ) {
        Page<Board> boards = boardService.findByUseYNAndTitleContaining('Y', search, pageable);

        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);

        return boardService.searchBoards(pageable, search);
    }
    /**
     * 필터
     */
    @GetMapping({"/recipe/filter"})
    public ResponseEntity<?> filterBoards(Model model,
                                          @PageableDefault(size = 5, sort = "recipeSeq", direction = Sort.Direction.DESC) Pageable pageable,
                                          @RequestParam(required = false, defaultValue = "") String filter
    ) {
        Page<Recipe> boards = recipeService.findByUseYNAndFoodClassTypeCode('Y', filter, pageable);

        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);

        return recipeService.filterBoards(pageable, filter);
    }

    /**
     * 조회수 높은 순
     */
    @GetMapping("/recipe/bestView")
    public ResponseEntity<?> bestViewBoards(
            Model model,
            @PageableDefault(size = 5, sort = "viewCnt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<Board> boards = boardService.findByUseYN('Y', pageable);
        log.debug("boards :: [{}]", pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return boardService.mainBoards(pageable);
    }
    /**
     즐겨찾기 높은 순
     */
    @GetMapping("/recipe/bestFavorited")
    public ResponseEntity<?> bestFavoritedBoards(
            Model model,
            @PageableDefault(size = 5, sort = "favorite", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<Board> boards = boardService.findByUseYN('Y', pageable);
        log.debug("boards :: [{}]", pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return boardService.mainBoards(pageable);
    }
    /**
     * 즐겨찾기한 게시판
     */
    @GetMapping("/recipe/favorited")
    public ResponseEntity<?> favoritedBoards(
            Model model,
            @PageableDefault(size = 5, direction = Sort.Direction.DESC) Pageable pageable,
            @ApiIgnore Authentication authentication

    ){
        Page<FavoriteBoard> boards = boardService.findByUseYNAndUser('Y', authentication, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return boardService.favoritedBoards(pageable, authentication);
    }

    /**
     *  즐겨찾기하기, 즐겨찾기취소하기
     */
    @PostMapping("/recipe/registerFavorited")      //회원 가입 버튼
    @ApiOperation(value = "레시피 즐겨찾기")
    public ResponseEntity<?> favoritedRegisterRecipe(@ApiParam(value = "게시글 id", required = true)@ApiIgnore Authentication authentication, BoardDto.boardFavoritedRegister boardFavoritedRegisterDto) {
        log.info("레시피 즐겨찾기");
        return boardService.favoritedRegisterRecipe(authentication, boardFavoritedRegisterDto);
    }

    /**
     레시피 디테일
     */
    @GetMapping("/recipe/view-detail")
    public ResponseEntity<?> viewRecipeDetail(
            RecipeDto.recipeDetail recipeDetailDto
    ){
        boardService.updateCount(recipeDetailDto.getRecipeSeq());
        return recipeService.viewRecipeDetail(recipeDetailDto);
    }
    @GetMapping("/recipe/view-ingredient-detail")
    public ResponseEntity<?> viewRecipeIngredientDetail(
            RecipeDto.recipeIngredientDetail recipeIngredientDetailDto
    ){
        return recipeService.viewRecipeIngredientDetail(recipeIngredientDetailDto);
    }
    @GetMapping("/recipe/view-course-detail")
    public ResponseEntity<?> viewRecipeCourseDetail(
            RecipeDto.recipeCourseDetail recipeCourseDetail
    ){
        return recipeService.viewRecipeCourseDetail(recipeCourseDetail);
    }
    @GetMapping("/recipe/view-reply")
    public ResponseEntity<?> viewRecipeReply(
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
    public ResponseEntity<?> save(@ApiIgnore Authentication authentication
            , RecipeDto.recipeCreate recipeDto) {
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