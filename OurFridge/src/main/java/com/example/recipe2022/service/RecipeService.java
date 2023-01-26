package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.RecipeVo;
import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.RecipeDto;
import com.example.recipe2022.data.entity.*;
import com.example.recipe2022.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecipeService {
    private final BoardRepository boardRepository;

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final Response response;
    private final CodesRepository codesRepository;
    private final RecipeCourseRepository recipeCourseRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ReplyRepository replyRepository;
    /**
     * 글작성 로직
     */

    @Transactional
    public ResponseEntity<?> createRecipe(Authentication authentication, RecipeDto.recipeCreate recipeDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        if(codesRepository.findByCodeNmContaining(recipeDto.getFoodClassName()).isEmpty()){
            return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        Codes codes = codesRepository.findByCodeNm(recipeDto.getFoodClassName());
        String CodeId = codes.getCodeId();
        Board boards = Board.builder()
                .title(recipeDto.getRecipeTitle())
                .contents(recipeDto.getRecipeContents())
                .fileId(recipeDto.getRecipeFile())
                .user(users)
                .boardDivCd("R")
                .build();
        boardRepository.save(boards);
        Recipe recipes = Recipe.builder()
                .title(recipeDto.getRecipeTitle())
                .contents(recipeDto.getRecipeContents())
                .file(recipeDto.getRecipeFile())
                .foodClassName(recipeDto.getFoodClassName())
                .foodClassTypeCode(CodeId)
                .volume(recipeDto.getRecipeVolume())
                .time(recipeDto.getRecipeTime())
                .level(recipeDto.getRecipeLevel())
                .user(users)
                .board(boards)
                .build();
        recipeRepository.save(recipes);

        return response.success("레시피가 생성되었습니다!");
    }
    public ResponseEntity<?> putIngredientToRecipe(int seq, int recipeSeq, RecipeDto.recipeIngredientCreate recipeIngredientDto) {
        if (!ingredientRepository.existsByIngredientId(seq)) {
            return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
//        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto();
        if (!recipeRepository.existsByRecipeId(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Ingredient ingredient = ingredientRepository.findByIngredientId(seq).orElseThrow();
        if(recipeIngredientRepository.existsByIngredient(ingredient)){
            return response.fail("중복된 재료입니다.", HttpStatus.BAD_REQUEST);
        }
        Recipe recipe = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .ingredient(ingredient)
                .recipe(recipe)
                .volume(recipeIngredientDto.getVolume())
                .build();
        recipeIngredientRepository.save(recipeIngredient);
        return response.success("n번 레시피 특정 재료 추가");
    }
    public ResponseEntity<?> putCourseToRecipe(int recipeSeq, RecipeDto.recipeCourseCreate recipeCourseDto){
        Recipe recipe = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if (!recipeRepository.existsByRecipeId(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        int currentCount = recipeCourseRepository.countByRecipe(recipe);
        RecipeCourse recipeCourse = RecipeCourse.builder()
                .recipeOrder(currentCount+1)
                .contents(recipeCourseDto.getContents())
                .fileId(recipeCourseDto.getRecipeFile())
                .tips(recipeCourseDto.getTips())
                .recipe(recipe)
                .build();
        recipeCourseRepository.save(recipeCourse);
        return response.success("n번 요리 과정 추가");

    }

    @Transactional
    public ResponseEntity<?> updateRecipe(int recipeSeq, RecipeDto.recipeCreate recipeDto){
        if(!recipeRepository.existsById(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(recipeSeq).orElseThrow();
        if(currentRecipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Board currentBoard = boardRepository.findById(recipeSeq).orElseThrow();
        String updateTitle = recipeDto.getRecipeTitle();
        String updateContents = recipeDto.getRecipeContents();
        String updateFiles = recipeDto.getRecipeFile();
        if(codesRepository.findByCodeNmContaining(recipeDto.getFoodClassName()).isEmpty()){
            return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        Codes codes = codesRepository.findByCodeNm(recipeDto.getFoodClassName());
        String CodeId = codes.getCodeId();
        String updateFoodClassName = recipeDto.getFoodClassName();
        String updateTime = recipeDto.getRecipeTime();
        int updateLevel = recipeDto.getRecipeLevel();
        currentRecipe.setTitle(updateTitle);
        currentRecipe.setContents(updateContents);
        currentRecipe.setFile(updateFiles);
        currentRecipe.setFoodClassName(updateFoodClassName);
        currentRecipe.setFoodClassTypeCode(CodeId);
        currentRecipe.setTime(updateTime);
        currentRecipe.setLevel(updateLevel);
        currentBoard.setTitle(updateTitle);
        currentBoard.setContents(updateContents);
        currentBoard.setFileId(updateFiles);
        recipeRepository.save(currentRecipe);
        boardRepository.save(currentBoard);
        return response.success("레시피 수정 성공 ");
    }

    @Transactional
    public ResponseEntity<?> updateRecipeIngredient(int ingSeq, int recipeSeq, RecipeDto.recipeIngredientCreate recipeIngredientDto){
        if (!recipeRepository.existsByRecipeId(recipeSeq)) {
            return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Recipe a = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Ingredient b = ingredientRepository.findByIngredientId(ingSeq).orElseThrow();
        if(!recipeIngredientRepository.existsByIngredient(b)){
            return response.fail("레시피에서 재료를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("현재 수정할려는 재료는 " +a.getRecipeSeq()+"번 레시피에서"+ b.getIngredientName() + "입니다");
        int seq = recipeIngredientRepository.findByRecipeAndIngredient(a, b).get().getRecipeIngredientSeq();
        RecipeIngredient currentRecipeIngredient = recipeIngredientRepository.findByRecipeIngredientSeq(seq);
        String updateVolume = recipeIngredientDto.getVolume();
        currentRecipeIngredient.setVolume(updateVolume);
        recipeIngredientRepository.save(currentRecipeIngredient);
        return response.success(recipeSeq+"번 레시피 특정 재료 수정");
    }

    @Transactional
    public ResponseEntity<?> updateCourseToRecipe(int order, int recipeSeq, RecipeDto.recipeCourseCreate recipeCourseDto) {
        if (!recipeRepository.existsByRecipeId(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        if (!recipeCourseRepository.existsByRecipeAndRecipeOrder(a, order)) {
            return response.fail("존재하지 않는 과정이에요", HttpStatus.BAD_REQUEST);
        }
        log.info("현재 수정할려는 과정은 " +a.getRecipeSeq()+"번 레시피에서"+ order + "번 입니다");
        int seq = recipeCourseRepository.findByRecipeAndRecipeOrder(a, order).get().getRecipeCourseSeq();
        RecipeCourse currentRecipeCourse = recipeCourseRepository.findByRecipeCourseSeq(seq);
        String updateContents = recipeCourseDto.getContents();
        String updateRecipeFile = recipeCourseDto.getRecipeFile();
        String tips = recipeCourseDto.getTips();
        currentRecipeCourse.setContents(updateContents);
        currentRecipeCourse.setFileId(updateRecipeFile);
        currentRecipeCourse.setTips(tips);
        recipeCourseRepository.save(currentRecipeCourse);
        return response.success(recipeSeq+"번 레시피 특정 과정 수정");
    }


    @Transactional
    public ResponseEntity<?> deleteRecipe(int recipeSeq){
        if(!recipeRepository.existsById(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다. ", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(recipeSeq).orElseThrow();
        if(currentRecipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Board currentBoard = boardRepository.findById(recipeSeq).orElseThrow();
        currentRecipe.setUseYN('N');
        currentBoard.setUseYN('N');
        recipeRepository.save(currentRecipe);
        boardRepository.save(currentBoard);
        return response.success("성공적으로 삭제되었습니다.");
    }

    @Transactional
    public ResponseEntity<?> deleteIngredientToRecipe(int ingSeq, int recipeSeq) {
        if (!ingredientRepository.existsByIngredientId(ingSeq)) {
            return response.fail("없는 재료에요", HttpStatus.BAD_REQUEST);
        }
        if (!recipeRepository.existsByRecipeId(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Ingredient b = ingredientRepository.findByIngredientId(ingSeq).orElseThrow();
        log.info("현재 삭제할려는 재료는 " +a.getRecipeSeq()+"번 레시피에서"+ b.getIngredientName() + "입니다");
        int seq = recipeIngredientRepository.findByRecipeAndIngredient(a, b).get().getRecipeIngredientSeq();
        recipeIngredientRepository.deleteByRecipeIngredientSeq(seq);
        return response.success(recipeSeq+"번 레시피 특정 재료 삭제");
    }

    @Transactional
    public ResponseEntity<?> deleteCourseToRecipe(int order, int recipeSeq) {
        if (!recipeRepository.existsByRecipeId(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        if (!recipeCourseRepository.existsByRecipeAndRecipeOrder(a, order)) {
            return response.fail("존재하지 않는 과정이에요", HttpStatus.BAD_REQUEST);
        }
        log.info("현재 삭제할려는 과정은 " +a.getRecipeSeq()+"번 레시피에서"+ order + "번 입니다");
        int seq = recipeCourseRepository.findByRecipeAndRecipeOrder(a, order).get().getRecipeCourseSeq();
        recipeCourseRepository.deleteByRecipeCourseSeq(seq);
//        RecipeCourse currentRecipeCourse = recipeCourseRepository.findByRecipeCourseSeq(seq);
//        currentRecipeCourse.setUseYN('N');
        List<RecipeCourse> recipeCourses = recipeCourseRepository.findAllByRecipe(a);
        for(int i=order; i<recipeCourses.size(); i++){
            int orders = recipeCourses.get(i).getRecipeOrder();
            recipeCourses.get(i).setRecipeOrder(orders-1);
        }
        return response.success(recipeSeq+"번 레시피 특정 과정 삭제");
    }




    /**
     * 글상세 로직
     */
    @Transactional
    public ResponseEntity<?> viewRecipeDetail(int recipeSeq) {
        if (!boardRepository.existsById(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        List<RecipeVo.recipeDetail> data = new ArrayList<>();
        log.info("현재 선택된 레시피는 " + recipe.getRecipeSeq() + "번입니다.");
        Users user = recipe.getUser();
        String userEmail = user.getEmail();
        Users users = userRepository.findByEmail(userEmail).orElseThrow();
        String userName = userRepository.findById(users.getId()).get().getName();
        RecipeVo.recipeDetail detailList = RecipeVo.recipeDetail.builder()
                .date(recipe.getModifiedDate())
                .userName(userName)
                .title(recipe.getTitle())
                .contents(recipe.getContents())
                .file(recipe.getFile())
                .foodClassName(recipe.getFoodClassName())
                .volume(recipe.getVolume())
                .time(recipe.getTime())
                .level(recipe.getLevel())
                .build();
        data.add(detailList);
        return response.success(data, recipeSeq + "번 레시피 조회", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> viewRecipeIngredientDetail(int recipeSeq) {
        if (!boardRepository.existsById(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        List<RecipeIngredient> recipeIngredient = recipeIngredientRepository.findAllByRecipe(recipe);
        List<RecipeVo.recipeIngredientDetail> data = new ArrayList<>();
        log.info("총 선택된 레시피재료는 " + recipeIngredient.size() + "개입니다.");
        for(RecipeIngredient recipeIngredients : recipeIngredient){
            RecipeVo.recipeIngredientDetail detailList = RecipeVo.recipeIngredientDetail.builder()
                    .ingredientName(recipeIngredients.getIngredient().getIngredientName())
                    .ingredientVolume(recipeIngredients.getVolume())
                    .build();
            data.add(detailList);
        }
        return response.success(data, recipeSeq + "번 레시피 조회", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> viewRecipeCourseDetail(int recipeSeq) {
        if (!boardRepository.existsById(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        List<RecipeCourse> recipeCourse = recipeCourseRepository.findAllByRecipe(recipe);
        List<RecipeVo.recipeCourseDetail> data = new ArrayList<>();
        log.info("총 선택된 레시피 과정은 " + recipeCourse.size() + "번입니다.");

        for(RecipeCourse recipeCourses : recipeCourse){
            RecipeVo.recipeCourseDetail detailList = RecipeVo.recipeCourseDetail.builder()
                    .recipeOrder(recipeCourses.getRecipeOrder())
                    .courseContents(recipeCourses.getContents())
                    .fileId(recipeCourses.getFileId())
                    .tips(recipeCourses.getTips())
                    .build();
            data.add(detailList);
        }
        return response.success(data, recipeSeq + "번 레시피 조회", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> viewRecipeReply(int recipeSeq) {
        if (!boardRepository.existsById(recipeSeq)) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeSeq).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Board board = boardRepository.findById(recipeSeq).orElseThrow();
        List<Reply> recipeReply = replyRepository.findAllByBoard(board);
        List<RecipeVo.recipeReply> data = new ArrayList<>();
        log.info("총 선택된 레시피 댓글은 " + recipeReply.size() + "번입니다.");

        for(Reply reply : recipeReply){
            Users user = reply.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getId()).get().getName();
            RecipeVo.recipeReply detailList = RecipeVo.recipeReply.builder()
                    .date(reply.getModifiedDate())
                    .name(userName)
                    .contents(reply.getContents())
                    .build();
            data.add(detailList);
        }
        return response.success(data, recipeSeq + "번 레시피 조회", HttpStatus.OK);
    }

    /**
     * 필터
     */
    @Transactional
    public ResponseEntity<?> filterBoards(Pageable pageable, String filter) {
        Page<Recipe> recipes = recipeRepository.findByUseYNAndFoodClassTypeCode('Y', filter, pageable);

        List<Map<String,Object>> list = new ArrayList<>();
        for(Recipe recipe : recipes){

            Map<String,Object> data = new HashMap<>();
            data.put("recipeId", recipe.getRecipeSeq());
            data.put("file", recipe.getFile());
            data.put("title", recipe.getTitle());

            data.put("count", recipe.getBoard().getViewCnt());
            log.debug("count :: data :: [{}]", recipe.getBoard().getViewCnt());
            data.put("name", recipe.getUser().getUsername());

            log.debug("searchBoards :: data :: [{}]", data);
            list.add(data);
        }
        log.debug("searchBoards :: list :: [{}]", list);

        int cnt = list.size();
        return response.success(list, "filterBoards :: "+cnt+"건 조회되었습니다!", HttpStatus.OK);
    }
    @Transactional
    public Page<Recipe> findByUseYNAndFoodClassTypeCode(Character useYN, String filter, Pageable pageable) {
        return recipeRepository.findByUseYNAndFoodClassTypeCode(useYN, filter, pageable);
    }





}