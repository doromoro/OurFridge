package com.example.recipe2022.controller;

import com.example.recipe2022.data.dao.RecipeVo;
import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.RecipeDto;
import com.example.recipe2022.data.entity.FavoriteRecipe;
import com.example.recipe2022.data.entity.Files;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.data.entity.RecipeCourse;
import com.example.recipe2022.data.enumer.FilePurpose;
import com.example.recipe2022.handler.general.FilesHandler;
import com.example.recipe2022.repository.FileRepository;
import com.example.recipe2022.repository.RecipeRepository;
import com.example.recipe2022.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeController {
    private final FileRepository fileRepository;
    private final RecipeRepository recipeRepository;
    @Autowired
    private RecipeService recipeService;

    private final FilesHandler fileHandler;

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
    @ResponseBody
    @PostMapping("/recipe/registerFavorited")      //회원 가입 버튼
    @ApiOperation(value = "레시피 즐겨찾기")
    public ResponseEntity<?> favoritedRegisterRecipe(@ApiParam(value = "게시글 id", required = true)@ApiIgnore Authentication authentication,
                                                     @RequestBody RecipeDto.recipeFavoritedRegister recipeFavoritedRegisterDto) {
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
    public ResponseEntity<?> saves(
              Authentication authentication
            , @RequestParam("create") String create
            , @RequestPart("recipeFiles") MultipartFile recipeFiles
            , @RequestPart("recipeCourseFiles") List<MultipartFile> recipeCourseFiles
//            , RecipeDto.recipe recipeDto
    ) throws Exception {
        log.info("레시피 등록");

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
        RecipeDto.recipe recipeDto = objectMapper.readValue(create, new TypeReference<RecipeDto.recipe>() {});

        // 레시피 create
        Map<String, Object> data = (Map<String, Object>) recipeService.createRecipe(authentication, create, recipeFiles).get("data");
        if(!data.isEmpty()){
            int recipeSeq = (int) data.get("recipeSeq");
            log.debug(String.valueOf(recipeSeq));


            // 레시피 재료 create

            log.debug(String.valueOf(recipeDto));

            List<RecipeDto.recipeIngredient> recipeIngredientList = recipeDto.getRecipeIngredientList();
            log.debug(recipeIngredientList.toString());

            for(RecipeDto.recipeIngredient recipeIngredient : recipeIngredientList){
                recipeIngredient.setRecipeSeq(recipeSeq);
                recipeService.putIngredientToRecipe(recipeIngredient);
            }

            // 1. RecipeDto.recipe 여기에다가 fileIdx, file_idx, file_index getter/setter 추가
            // 2. create => {recipeCourceList:[ {fileIdx:""}, {fileIdx:""} // 파일을 첨부한 레시피 과정
            // , {}, {}, {} ]} // 파일을 첨부하지 않은 레시피 과정

            // TODO :: fileIdx를 언제 부여하느냐
            // {recipeCourceList:[ {}, {fileIdx:"1"}, {}, {fileIdx:"2"}, {} ]};     6
            // recipeCourseFiles = [{2번째 과정 파일}, {4번째 과정 파일}];      2

            // 레시피 과정 create
            List<RecipeDto.recipeCourse> recipeCourseList = recipeDto.getRecipeCourseList();
            int ord = 1;
            // int a = 0;
            for(RecipeDto.recipeCourse recipeCourse : recipeCourseList){
                Files picture = fileRepository.findByFileSeq(0);

                int fileIdx = recipeCourse.getFileIdx()-1;
                // 파일을 첨부한 레시피 과정
                if(-1<fileIdx) picture =  fileHandler.parseFileInfo(FilePurpose.COURSE_PICTURE, recipeCourseFiles.get(fileIdx));
                fileRepository.save(picture);
                recipeCourse.setRecipeSeq(recipeSeq);
                recipeCourse.setRecipeFile(picture);
                recipeCourse.setOrder(ord);
                recipeService.putCourseToRecipe(recipeCourse);
                ord ++;
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
        return response.success("삭제 완료");
    }

//    @PostMapping(value = "/recipe/delete-ingredient")
//    @ApiOperation(value = "n번 레시피에서 재료 삭제")
//    public ResponseEntity<?> deleteIngredientToRecipe(
//            RecipeDto.recipeIngredientDelete recipeIngredientDeleteDto
//    ){
//        log.info("레시피에 재료 삭제");
//        return recipeService.deleteIngredientToRecipe(recipeIngredientDeleteDto);
//    }

//    @PostMapping(value = "/recipe/delete-course")
//    @ApiOperation(value = "n번 레시피에서 과정 삭제")
//    public ResponseEntity<?> deleteCourseToRecipe(
//            RecipeDto.recipeCourseDelete recipeCourseDeleteDto
//    ){
//        log.info("레시피에 특정 과정 삭제");
//        return recipeService.deleteCourseToRecipe(recipeCourseDeleteDto);
//    }

    @PostMapping(value = "/recipe-update")
    @ApiOperation(value = "레시피 수정")
    public ResponseEntity<?> updateRecipe(
              Authentication authentication
            , @RequestParam("update") String update
            , @RequestPart("recipeFiles") MultipartFile recipeFiles
            , @RequestPart("recipeCourseFiles") List<MultipartFile> recipeCourseFiles
//            , @RequestBody RecipeDto.recipe recipeDto
    ) {

        log.info("==================== 레시피 수정 START ====================");

        Map<String, Object> result = new HashMap<>();
        String message = "";

        try{
            // 레시피 seq
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule());
            RecipeDto.recipe recipeInfo = objectMapper.readValue(update, new TypeReference<RecipeDto.recipe>() {});
            int recipeSeq = recipeInfo.getRecipeSeq();
            log.info("recipeSeq : {} ", recipeSeq);

            // 레시피 update
            result = recipeService.updateRecipe(authentication, update, recipeFiles);
            if(("200").equals(result.get("code").toString())){
                // 레시피 재료 delete
                List<RecipeDto.recipeIngredient> recipeIngredientDeleteList = recipeInfo.getRecipeIngredientDeleteList();
                if(null != recipeIngredientDeleteList){
                    for(RecipeDto.recipeIngredient recipeIngredient : recipeIngredientDeleteList){
                        recipeIngredient.setRecipeSeq(recipeSeq);
                        result = recipeService.deleteIngredientToRecipe(recipeIngredient);
                        if(!("200").equals(result.get("code").toString())){
                            message = result.get("message").toString();
                            throw new Exception();
                        }
                    }
                }

                // 레시피 재료 insert/update
                List<RecipeDto.recipeIngredient> recipeIngredientList = recipeInfo.getRecipeIngredientList();
                for(RecipeDto.recipeIngredient recipeIngredient : recipeIngredientList){
                    recipeIngredient.setRecipeSeq(recipeSeq);
                    // 신규 입력일 때
                    // TODO DTO 컬럼값 세팅 중 NULL값에 대한 처리 필요
//                if(null == (Integer) recipeIngredient.getRecipeIngredientSeq()){
                    if(recipeIngredient.getRecipeIngredientSeq()<1){
                        result = recipeService.putIngredientToRecipe(recipeIngredient);
                        if(!("200").equals(result.get("code").toString())){
                            message = result.get("message").toString();
                            throw new Exception();
                        }
                    }else{
                        // 업데이트일 때
                        result = recipeService.updateRecipeIngredient(recipeIngredient);;
                        if(!("200").equals(result.get("code").toString())){
                            message = result.get("message").toString();
                            throw new Exception();
                        }
                    }
                }

                // 레시피 과정 delete
                List<RecipeDto.recipeCourse> recipeCourseDeleteList = recipeInfo.getRecipeCourseDeleteList();
                if(null != recipeCourseDeleteList){
                    for(RecipeDto.recipeCourse recipeCourse : recipeCourseDeleteList){
                        recipeCourse.setRecipeSeq(recipeSeq);
                        result = recipeService.deleteCourseToRecipe(recipeCourse);
                        if(!("200").equals(result.get("code").toString())){
                            message = result.get("message").toString();
                            throw new Exception();
                        }
                    }
                }

                // 레시피 과정 insert/update

                // 레시피 과정 첨부파일 (사진) 케이스
                // case1. 과정 신규
                // case1-1. 과정 신규 / 과정 파일 신규 등록
                // case1-2. 과정 신규 / 과정 파일 미등록

                // case2. 과정 수정
                // case2-1. 과정 수정 / 과정 파일 신규 등록
                // case2-2. 과정 수정 / 과정 파일 수정
                // case2-3. 과정 수정 / 과정 파일 삭제
                // case2-4. 과정 수정 / 과정 파일 수정 X

                List<RecipeDto.recipeCourse> recipeCourseList = recipeInfo.getRecipeCourseList();
                int ord = 1;
                for(RecipeDto.recipeCourse recipeCourse : recipeCourseList){
                    recipeCourse.setRecipeSeq(recipeSeq);
                    recipeCourse.setOrder(ord);
                    // 신규 입력일 때
                    // TODO DTO 컬럼값 세팅 중 NULL값에 대한 처리 필요

                    // case1. 과정 신규
                    // case1-1. 과정 신규 / 과정 파일 신규 등록
                    // case1-2. 과정 신규 / 과정 파일 미등록
                    Files picture = fileRepository.findByFileSeq(0);
                    int fileIdx = recipeCourse.getFileIdx()-1;
                    if(recipeCourse.getRecipeCourseSeq()<1){
                        if(-1<fileIdx) picture =  fileHandler.parseFileInfo(FilePurpose.COURSE_PICTURE, recipeCourseFiles.get(fileIdx));
                        fileRepository.save(picture);
                        recipeCourse.setRecipeFile(picture);

                        result = recipeService.putCourseToRecipe(recipeCourse);
                        // 첨부파일 처리 로직 구현
                        // 레시피 신규 등록 로직과 동일
                        if(!("200").equals(result.get("code").toString())){
                            message = result.get("message").toString();
                            throw new Exception();
                        }
                    }else{
                        // 업데이트일 때
                        Files files = recipeCourse.getRecipeFile();
                        // case2. 과정 수정
                        // case2-4. 과정 수정 / 과정 파일 수정 X
                        // {recipeCourseSeq:"1", fileSeq:"5"}
                        fileIdx = recipeCourse.getFileIdx();
                        if(fileIdx != 0){
                            // case2-3. 과정 수정 / 과정 파일 삭제
                            // {recipeCourseSeq:"1", fileIdx:"-1", fileSeq:"100"}
                            if(fileIdx<0){
                                // 파일 테이블에 해당 fileSeq의 use_yn 컬럼을 N으로 update
                                files.setUseYN('N');
                                fileRepository.save(files);
                                // 레시피 과정 테이블에 해당 recipeCourseSeq의 fileSeq 컬럼을 0으로 update
                                recipeCourse.setRecipeFile(picture);
                            }else{
                                // case2-1. 과정 수정 / 과정 파일 신규 등록
                                // {recipeCourseSeq:"1", fileIdx:"1", fileSeq:"0"}
                                // case2-2. 과정 수정 / 과정 파일 수정
                                // {recipeCourseSeq:"1", fileIdx:"2", fileSeq:"100"}
                                fileIdx--;
                                if(0<recipeCourse.getRecipeFile().getFileSeq()){
                                    // 파일 테이블에 해당 fileSeq의 use_yn 컬럼을 N으로 update
                                    files.setUseYN('N');
                                    fileRepository.save(files);
                                }
                                // 파일 테이블에 idx에 해당하는 파일 insert
                                picture =  fileHandler.parseFileInfo(FilePurpose.COURSE_PICTURE, recipeCourseFiles.get(fileIdx));
                                fileRepository.save(picture);
                                // 레시피 과정 테이블에 해당 recipeCourseSeq의 fileSeq 컬럼을 신규 등록한 fileSeq로 update
                                recipeCourse.setRecipeFile(picture);
                            }
                        }

                        result = recipeService.updateCourseToRecipe(recipeCourse);
                        if(!("200").equals(result.get("code").toString())){
                            message = result.get("message").toString();
                            throw new Exception();
                        }
                    }
                    ord ++;
                }
            }else{
                message = result.get("message").toString();
                throw new Exception();
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("레시피 수정에 실패하였습니다. :: {}", e.getMessage());
            return response.fail(message, HttpStatus.BAD_REQUEST);
        }
        log.info("==================== 레시피 수정 END ====================");
        return response.success("레시피 수정 성공");
    }
//    @PostMapping(value = "/recipe/update-ingredient")
//    @ApiOperation(value = "n번 레시피에서 재료 수정")
//    public ResponseEntity<?> updateRecipeIngredient(
//              RecipeDto.recipeIngredientUpdate recipeIngredientDto
//    )
//    {
//        log.info("레시피에 재료 수정");
//        return recipeService.updateRecipeIngredient(recipeIngredientDto);
//    }

//    @PostMapping(value = "/recipe/update-course")
//    @ApiOperation(value = "n번 레시피에서 과정 수정")
//    public ResponseEntity<?> updateCourseToRecipe(
//            RecipeDto.recipeCourseUpdate recipeCourseDto
//    )
//    {
//        log.info("레시피에 과정 수정");
//        return recipeService.updateCourseToRecipe(recipeCourseDto);
//    }
}