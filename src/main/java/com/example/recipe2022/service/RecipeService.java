package com.example.recipe2022.service;

import com.example.recipe2022.model.data.*;
import com.example.recipe2022.model.dto.RecipeDto;
import com.example.recipe2022.model.repository.*;
import com.example.recipe2022.model.vo.RecipeVo;
import com.example.recipe2022.model.vo.Response;
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
                .file_grp_id(recipeDto.getRecipeFile())
                .user(users)
                .boardDiv("R")
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
    public ResponseEntity<?> putIngredientToRecipe(RecipeDto.recipeIngredientCreate recipeIngredientDto) {
        if (!ingredientRepository.existsByIngredientId(recipeIngredientDto.getIngSeq())) {
            return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        if (!recipeRepository.existsByRecipeId(recipeIngredientDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Ingredient ingredient = ingredientRepository.findByIngredientId(recipeIngredientDto.getIngSeq()).orElseThrow();
        if(recipeIngredientRepository.existsByIngredient(ingredient)){
            return response.fail("중복된 재료입니다.", HttpStatus.BAD_REQUEST);
        }
        Recipe recipe = recipeRepository.findByRecipeId(recipeIngredientDto.getRecipeSeq()).orElseThrow();
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .ingredient(ingredient)
                .recipe(recipe)
                .volume(recipeIngredientDto.getVolume())
                .build();
        recipeIngredientRepository.save(recipeIngredient);
        return response.success("n번 레시피 특정 재료 추가");
    }

    public ResponseEntity<?> putCourseToRecipe(RecipeDto.recipeCourseCreate recipeCourseDto){
        Recipe recipe = recipeRepository.findByRecipeId(recipeCourseDto.getRecipeSeq()).orElseThrow();
        if (!recipeRepository.existsByRecipeId(recipeCourseDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        int currentCount = recipeCourseRepository.countByRecipe(recipe);
        RecipeCourse recipeCourse = RecipeCourse.builder()
                .recipeOrder(currentCount+1)
                .contents(recipeCourseDto.getContents())
                .file_grp_id(recipeCourseDto.getRecipeFile())
                .tips(recipeCourseDto.getTips())
                .recipe(recipe)
                .build();
        recipeCourseRepository.save(recipeCourse);
        return response.success("n번 요리 과정 추가");

    }

    @Transactional
    public ResponseEntity<?> updateRecipe(RecipeDto.recipeUpdate recipeDto){
        if(!recipeRepository.existsById(recipeDto.getRecipeSeq())){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(recipeDto.getRecipeSeq()).orElseThrow();
        if(currentRecipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Board currentBoard = boardRepository.findById(recipeDto.getRecipeSeq()).orElseThrow();
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
        currentBoard.setFile_grp_id(updateFiles);
        recipeRepository.save(currentRecipe);
        boardRepository.save(currentBoard);
        return response.success("레시피 수정 성공 ");
    }

    @Transactional
    public ResponseEntity<?> updateRecipeIngredient(RecipeDto.recipeIngredientUpdate recipeIngredientDto){
        if (!recipeRepository.existsByRecipeId(recipeIngredientDto.getRecipeSeq())) {
            return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Recipe a = recipeRepository.findByRecipeId(recipeIngredientDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Ingredient b = ingredientRepository.findByIngredientId(recipeIngredientDto.getIngSeq()).orElseThrow();
        if(!recipeIngredientRepository.existsByIngredient(b)){
            return response.fail("레시피에서 재료를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("현재 수정할려는 재료는 " +a.getRecipeId()+"번 레시피에서"+ b.getIngredientName() + "입니다");
        int seq = recipeIngredientRepository.findByRecipeAndIngredient(a, b).get().getRecipeIngredientSeq();
        RecipeIngredient currentRecipeIngredient = recipeIngredientRepository.findByRecipeIngredientSeq(seq);
        String updateVolume = recipeIngredientDto.getVolume();
        currentRecipeIngredient.setVolume(updateVolume);
        recipeIngredientRepository.save(currentRecipeIngredient);
        return response.success(recipeIngredientDto.getRecipeSeq()+"번 레시피 특정 재료 수정");
    }

    @Transactional
    public ResponseEntity<?> updateCourseToRecipe(RecipeDto.recipeCourseUpdate recipeCourseDto) {
        if (!recipeRepository.existsByRecipeId(recipeCourseDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeId(recipeCourseDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        if (!recipeCourseRepository.existsByRecipeAndRecipeOrder(a, recipeCourseDto.getOrder())) {
            return response.fail("존재하지 않는 과정이에요", HttpStatus.BAD_REQUEST);
        }
        log.info("현재 수정할려는 과정은 " +a.getRecipeId()+"번 레시피에서"+ recipeCourseDto.getOrder() + "번 입니다");
        int seq = recipeCourseRepository.findByRecipeAndRecipeOrder(a, recipeCourseDto.getOrder()).get().getRecipeCourseSeq();
        RecipeCourse currentRecipeCourse = recipeCourseRepository.findByRecipeCourseSeq(seq);
        String updateContents = recipeCourseDto.getContents();
        String updateRecipeFile = recipeCourseDto.getRecipeFile();
        String tips = recipeCourseDto.getTips();
        currentRecipeCourse.setContents(updateContents);
        currentRecipeCourse.setFile_grp_id(updateRecipeFile);
        currentRecipeCourse.setTips(tips);
        recipeCourseRepository.save(currentRecipeCourse);
        return response.success(recipeCourseDto.getRecipeSeq()+"번 레시피 특정 과정 수정");
    }


    @Transactional
    public ResponseEntity<?> deleteRecipe(RecipeDto.recipeDelete recipeDeleteDto){
        if(!recipeRepository.existsById(recipeDeleteDto.getRecipeSeq())){
            return response.fail("레시피를 찾을 수 없습니다. ", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(recipeDeleteDto.getRecipeSeq()).orElseThrow();
        if(currentRecipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Board currentBoard = boardRepository.findById(recipeDeleteDto.getRecipeSeq()).orElseThrow();
        currentRecipe.setUseYN('N');
        currentBoard.setUseYN('N');
//        recipeRepository.deleteById(recipeSeq);
//        boardRepository.deleteById(recipeSeq);
        recipeRepository.save(currentRecipe);
        boardRepository.save(currentBoard);
        return response.success("성공적으로 삭제되었습니다.");
    }

    @Transactional
    public ResponseEntity<?> deleteIngredientToRecipe(RecipeDto.recipeIngredientDelete recipeIngredientDeleteDto) {
        if (!ingredientRepository.existsByIngredientId(recipeIngredientDeleteDto.getIngSeq())) {
            return response.fail("없는 재료에요", HttpStatus.BAD_REQUEST);
        }
        if (!recipeRepository.existsByRecipeId(recipeIngredientDeleteDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeId(recipeIngredientDeleteDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Ingredient b = ingredientRepository.findByIngredientId(recipeIngredientDeleteDto.getIngSeq()).orElseThrow();
        log.info("현재 삭제할려는 재료는 " +a.getRecipeId()+"번 레시피에서"+ b.getIngredientName() + "입니다");
        int seq = recipeIngredientRepository.findByRecipeAndIngredient(a, b).get().getRecipeIngredientSeq();
        recipeIngredientRepository.deleteByRecipeIngredientSeq(seq);
        return response.success(recipeIngredientDeleteDto.getRecipeSeq()+"번 레시피 특정 재료 삭제");
    }

    @Transactional
    public ResponseEntity<?> deleteCourseToRecipe(RecipeDto.recipeCourseDelete recipeCourseDeleteDto) {
        if (!recipeRepository.existsByRecipeId(recipeCourseDeleteDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeId(recipeCourseDeleteDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        if (!recipeCourseRepository.existsByRecipeAndRecipeOrder(a, recipeCourseDeleteDto.getOrder())) {
            return response.fail("존재하지 않는 과정이에요", HttpStatus.BAD_REQUEST);
        }
        log.info("현재 삭제할려는 과정은 " +a.getRecipeId()+"번 레시피에서"+ recipeCourseDeleteDto.getOrder() + "번 입니다");
        int seq = recipeCourseRepository.findByRecipeAndRecipeOrder(a, recipeCourseDeleteDto.getOrder()).get().getRecipeCourseSeq();
        recipeCourseRepository.deleteByRecipeCourseSeq(seq);
//        RecipeCourse currentRecipeCourse = recipeCourseRepository.findByRecipeCourseSeq(seq);
//        currentRecipeCourse.setUseYN('N');
        List<RecipeCourse> recipeCourses = recipeCourseRepository.findAllByRecipe(a);
        for(int i=recipeCourseDeleteDto.getOrder()-1; i<recipeCourses.size(); i++){
            int orders = recipeCourses.get(i).getRecipeOrder();
            recipeCourses.get(i).setRecipeOrder(orders-1);
        }
        return response.success(recipeCourseDeleteDto.getRecipeSeq()+"번 레시피 특정 과정 삭제");
    }




    /**
     * 글상세 로직
     */
    @Transactional
    public ResponseEntity<?> viewRecipeDetail(RecipeDto.recipeDetail recipeDetailDto) {
        if (!boardRepository.existsById(recipeDetailDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeDetailDto.getRecipeSeq()).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        List<RecipeVo.recipeDetail> data = new ArrayList<>();
        log.info("현재 선택된 레시피는 " + recipe.getRecipeId() + "번입니다.");
        Users user = recipe.getUser();
        String userEmail = user.getUserEmail();
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
        return response.success(data, recipeDetailDto.getRecipeSeq() + "번 레시피 조회", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> viewRecipeIngredientDetail(RecipeDto.recipeIngredientDetail recipeIngredientDetailDto) {
        if (!boardRepository.existsById(recipeIngredientDetailDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeIngredientDetailDto.getRecipeSeq()).orElseThrow();
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
        return response.success(data, recipeIngredientDetailDto.getRecipeSeq() + "번 레시피 조회", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> viewRecipeCourseDetail(RecipeDto.recipeCourseDetail recipeCourseDetailDto) {
        if (!boardRepository.existsById(recipeCourseDetailDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeCourseDetailDto.getRecipeSeq()).orElseThrow();
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
                    .file_grp_id(recipeCourses.getFile_grp_id())
                    .tips(recipeCourses.getTips())
                    .build();
            data.add(detailList);
        }
        return response.success(data, recipeCourseDetailDto.getRecipeSeq() + "번 레시피 조회", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> viewRecipeReply(RecipeDto.recipeReply recipeReplyDto) {
        if (!boardRepository.existsById(recipeReplyDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeId(recipeReplyDto.getRecipeSeq()).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Board board = boardRepository.findById(recipeReplyDto.getRecipeSeq()).orElseThrow();
        List<Reply> recipeReply = replyRepository.findAllByBoard(board);
        List<RecipeVo.recipeReply> data = new ArrayList<>();
        log.info("총 선택된 레시피 댓글은 " + recipeReply.size() + "번입니다.");

        for(Reply reply : recipeReply){
            Users user = reply.getUser();
            String userEmail = user.getUserEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getId()).get().getName();
            RecipeVo.recipeReply detailList = RecipeVo.recipeReply.builder()
                    .date(reply.getModifiedDate())
                    .name(userName)
                    .contents(reply.getContents())
                    .build();
            data.add(detailList);
        }
        return response.success(data, recipeReplyDto.getRecipeSeq() + "번 레시피 조회", HttpStatus.OK);
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
            data.put("recipeId", recipe.getRecipeId());
            data.put("file", recipe.getFile());
            data.put("title", recipe.getTitle());

            data.put("count", recipe.getBoard().getView());
            log.debug("count :: data :: [{}]", recipe.getBoard().getView());
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
