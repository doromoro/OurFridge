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

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final Response response;
    private final CodesRepository codesRepository;
    private final RecipeCourseRepository recipeCourseRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ReplyRepository replyRepository;
    private final FavoriteRecipeRepository favoriteRecipeRepository;

    private Map<String, Object> returnRst(Object data, String message, HttpStatus httpStatus){
        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        result.put("message", message);
        result.put("code", httpStatus);
        return result;
    }

    /**
     * 글작성 로직
     */

    @Transactional
    public Map<String, Object> createRecipe(Authentication authentication
            , RecipeDto.recipe RecipeDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        if(codesRepository.findByCodeNmContaining(RecipeDto.getFoodClassName()).isEmpty()){
            return returnRst(new HashMap<>(),"검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        Codes codes = codesRepository.findByCodeNm(RecipeDto.getFoodClassName());
        String CodeId = codes.getCodeId();
        Recipe recipes = Recipe.builder()
                .title(RecipeDto.getRecipeTitle())
                .contents(RecipeDto.getRecipeContents())
                .file(RecipeDto.getRecipeFile())
                .foodClassName(RecipeDto.getFoodClassName())
                .foodClassTypeCode(CodeId)
                .volume(RecipeDto.getRecipeVolume())
                .time(RecipeDto.getRecipeTime())
                .level(RecipeDto.getRecipeLevel())
                .user(users)
                .build();
        recipeRepository.save(recipes);

        Map<String, Object> data = new HashMap<>();
        data.put("recipeSeq", recipes.getRecipeSeq());
        return returnRst(data, "레시피 생성", HttpStatus.OK);
//        return response.success("레시피가 생성되었습니다!");
    }
    public ResponseEntity<?> putIngredientToRecipe(RecipeDto.recipeIngredient recipeIngredientDto) {
        if (!ingredientRepository.existsByIngredientId(recipeIngredientDto.getIngSeq())) {
            return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        if (!recipeRepository.existsByRecipeSeq(recipeIngredientDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Ingredient ingredient = ingredientRepository.findByIngredientId(recipeIngredientDto.getIngSeq()).orElseThrow();

        // TODO RECIPE_SEQ 에 해당하는 INGREDIENT_SEQ 중복 체크 필요
//        if(recipeIngredientRepository.existsByIngredient(ingredient)){
//            return response.fail("중복된 재료입니다.", HttpStatus.BAD_REQUEST);
//        }
        Recipe recipe = recipeRepository.findByRecipeSeq(recipeIngredientDto.getRecipeSeq()).orElseThrow();
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                .ingredient(ingredient)
                .recipe(recipe)
                .volume(recipeIngredientDto.getVolume())
                .build();
        recipeIngredientRepository.save(recipeIngredient);
        return response.success("n번 레시피 특정 재료 추가");
    }

    public ResponseEntity<?> putCourseToRecipe(RecipeDto.recipeCourse recipeCourseDto){
        Recipe recipe = recipeRepository.findByRecipeSeq(recipeCourseDto.getRecipeSeq()).orElseThrow();
        if (!recipeRepository.existsByRecipeSeq(recipeCourseDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        // int currentCount = recipeCourseRepository.countByRecipe(recipe);
        RecipeCourse recipeCourse = RecipeCourse.builder()
                .recipeOrder(recipeCourseDto.getOrder())
                .contents(recipeCourseDto.getContents())
                .fileId(recipeCourseDto.getRecipeFile())
                .tips(recipeCourseDto.getTips())
                .recipe(recipe)
                .build();
        recipeCourseRepository.save(recipeCourse);
        return response.success("n번 요리 과정 추가");

    }


    @Transactional
    public Map<String, Object> updateRecipe(Authentication authentication, RecipeDto.recipe RecipeDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        if (userRepository.findByEmail(email).orElse(null) == null) {
            return returnRst(new HashMap<>(),"해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Users users = userRepository.findByEmail(email).orElseThrow();
        if(!recipeRepository.existsById(RecipeDto.getRecipeSeq())){
            return returnRst(new HashMap<>(),"레시피를 찾을 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(RecipeDto.getRecipeSeq()).orElseThrow();
        if(currentRecipe.getUseYN() == 'N'){
            return returnRst(new HashMap<>(),"해당 레시피가 이미 삭제되었습니다.", HttpStatus.BAD_REQUEST);
        }
        if(!users.getRecipes().contains(currentRecipe)){
            return returnRst(new HashMap<>(), "삭제할수 있는 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        String updateTitle = RecipeDto.getRecipeTitle();
        String updateContents = RecipeDto.getRecipeContents();
        String updateFiles = RecipeDto.getRecipeFile();
        if(codesRepository.findByCodeNmContaining(RecipeDto.getFoodClassName()).isEmpty()){
            return returnRst(new HashMap<>(), "검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }
        Codes codes = codesRepository.findByCodeNm(RecipeDto.getFoodClassName());
        String CodeId = codes.getCodeId();
        String updateFoodClassName = RecipeDto.getFoodClassName();
        String updateTime = RecipeDto.getRecipeTime();
        int updateLevel = RecipeDto.getRecipeLevel();
        currentRecipe.setTitle(updateTitle);
        currentRecipe.setContents(updateContents);
        currentRecipe.setFile(updateFiles);
        currentRecipe.setFoodClassName(updateFoodClassName);
        currentRecipe.setFoodClassTypeCode(CodeId);
        currentRecipe.setTime(updateTime);
        currentRecipe.setLevel(updateLevel);
        recipeRepository.save(currentRecipe);

        Map<String, Object> data = new HashMap<>();
        data.put("recipeSeq", currentRecipe.getRecipeSeq());
        return returnRst(data, "레시피 수정", HttpStatus.OK);

//        return response.success("레시피 수정 성공 ");
    }

    @Transactional
    public ResponseEntity<?> updateRecipeIngredient(RecipeDto.recipeIngredient recipeIngredientDto){
        if (!recipeRepository.existsByRecipeSeq(recipeIngredientDto.getRecipeSeq())) {
            return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Recipe a = recipeRepository.findByRecipeSeq(recipeIngredientDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Ingredient b = ingredientRepository.findByIngredientId(recipeIngredientDto.getIngSeq()).orElseThrow();
//        if(!recipeIngredientRepository.existsByIngredient(b)){
        if(null == b){
            return response.fail("레시피에서 재료를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

//        log.info("현재 수정할려는 재료는 " +a.getRecipeSeq()+"번 레시피에서"+ b.getIngredientName() + "입니다");
//        int seq = recipeIngredientRepository.findByRecipeAndIngredient(a, b).get().getRecipeIngredientSeq();
        RecipeIngredient currentRecipeIngredient = recipeIngredientRepository.findByRecipeIngredientSeq(recipeIngredientDto.getRecipeIngredientSeq());
        String updateVolume = recipeIngredientDto.getVolume();
        currentRecipeIngredient.setVolume(updateVolume);
        recipeIngredientRepository.save(currentRecipeIngredient);
        return response.success(recipeIngredientDto.getRecipeSeq()+"번 레시피 특정 재료 수정");
    }

    @Transactional
    public ResponseEntity<?> updateCourseToRecipe(RecipeDto.recipeCourse recipeCourseDto) {
        if (!recipeRepository.existsByRecipeSeq(recipeCourseDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeSeq(recipeCourseDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            // TODO 종속된 레시피 과정 USE_YN = 'N' 처리나 물리 삭제 필요
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
//        if (!recipeCourseRepository.existsByRecipeAndRecipeOrder(a, recipeCourseDto.getOrder())) {
//            return response.fail("존재하지 않는 과정이에요", HttpStatus.BAD_REQUEST);
//        }
//        log.info("현재 수정할려는 과정은 " +a.getRecipeSeq()+"번 레시피에서"+ recipeCourseDto.getOrder() + "번 입니다");
//        int seq = recipeCourseRepository.findByRecipeAndRecipeOrder(a, recipeCourseDto.getOrder()).get().getRecipeCourseSeq();
//        RecipeCourse currentRecipeCourse = recipeCourseRepository.findByRecipeCourseSeq(seq);
//        String updateContents = recipeCourseDto.getContents();
//        String updateRecipeFile = recipeCourseDto.getRecipeFile();
//        String tips = recipeCourseDto.getTips();
//        currentRecipeCourse.setContents(updateContents);
//        currentRecipeCourse.setFileId(updateRecipeFile);
//        currentRecipeCourse.setTips(tips);

        RecipeCourse recipeCourse = new RecipeCourse();
        recipeCourse.setRecipe(a);
        recipeCourse.setRecipeCourseSeq(recipeCourseDto.getRecipeCourseSeq());
        recipeCourse.setRecipeOrder(recipeCourseDto.getOrder());
        recipeCourse.setContents(recipeCourseDto.getContents());
        recipeCourse.setFileId(recipeCourseDto.getRecipeFile());
        recipeCourse.setTips(recipeCourseDto.getTips());

        recipeCourseRepository.save(recipeCourse);
        return response.success(recipeCourseDto.getRecipeSeq()+"번 레시피 특정 과정 수정");
    }


    @Transactional
    public Map<String, Object> deleteRecipe(Authentication authentication, RecipeDto.recipeDelete recipeDeleteDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        if (userRepository.findByEmail(email).orElse(null) == null) {
            return returnRst(new HashMap<>(),"해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Users users = userRepository.findByEmail(email).orElseThrow();
        if(!recipeRepository.existsById(recipeDeleteDto.getRecipeSeq())){
            return returnRst(new HashMap<>(),"레시피를 찾을 수 없습니다", HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findById(recipeDeleteDto.getRecipeSeq()).orElseThrow();
//        if(!users.getRecipes().contains(currentRecipe)){
//            return returnRst(new HashMap<>(), "삭제할수 있는 권한이 없습니다", HttpStatus.BAD_REQUEST);
//        }
        if(currentRecipe.getUseYN() == 'N'){
            return returnRst(new HashMap<>(),"이미 삭제된 레시피입니다.", HttpStatus.BAD_REQUEST);
        }
        currentRecipe.setUseYN('N');
        recipeRepository.save(currentRecipe);

        // TODO 현재 레시피 재료, 과정, 댓글 물리 삭제
        // 레시피 재료 삭제
        recipeIngredientRepository.deleteAllByRecipe(currentRecipe);
        // 레시피 과정 삭제
        recipeCourseRepository.deleteAllByRecipe(currentRecipe);
        // 댓글 삭제
        replyRepository.deleteAllByRecipe(currentRecipe);

//        recipeRepository.deleteById(recipeSeq);
        return returnRst(new HashMap<>(), "레시피 삭제", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteIngredientToRecipe(RecipeDto.recipeIngredient recipeIngredientDeleteDto) {
//        if (!ingredientRepository.existsByIngredientId(recipeIngredientDeleteDto.getIngSeq())) {
//            return response.fail("없는 재료에요", HttpStatus.BAD_REQUEST);
//        }
        if (!recipeRepository.existsByRecipeSeq(recipeIngredientDeleteDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeSeq(recipeIngredientDeleteDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("이미 삭제된 레시피입니다.",HttpStatus.BAD_REQUEST);
        }
//        Ingredient b = ingredientRepository.findByIngredientId(recipeIngredientDeleteDto.getIngSeq()).orElseThrow();
//        log.info("현재 삭제할려는 재료는 " +a.getRecipeSeq()+"번 레시피에서"+ b.getIngredientName() + "입니다");
//        int seq = recipeIngredientRepository.findByRecipeAndIngredient(a, b).get().getRecipeIngredientSeq();
        recipeIngredientRepository.deleteByRecipeIngredientSeq(recipeIngredientDeleteDto.getRecipeIngredientSeq());
        return response.success(recipeIngredientDeleteDto.getRecipeSeq()+"번 레시피 특정 재료 삭제");
    }

    @Transactional
    public ResponseEntity<?> deleteCourseToRecipe(RecipeDto.recipeCourse recipeCourseDeleteDto) {
        if (!recipeRepository.existsByRecipeSeq(recipeCourseDeleteDto.getRecipeSeq())) { return response.fail("레시피를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe a = recipeRepository.findByRecipeSeq(recipeCourseDeleteDto.getRecipeSeq()).orElseThrow();
        if(a.getUseYN() == 'N'){
            return response.fail("해당 레시피가 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
//        if (!recipeCourseRepository.existsByRecipeAndRecipeOrder(a, recipeCourseDeleteDto.getOrder())) {
//            return response.fail("존재하지 않는 과정이에요", HttpStatus.BAD_REQUEST);
//        }
//        log.info("현재 삭제할려는 과정은 " +a.getRecipeSeq()+"번 레시피에서"+ recipeCourseDeleteDto.getOrder() + "번 입니다");
//        int seq = recipeCourseRepository.findByRecipeAndRecipeOrder(a, recipeCourseDeleteDto.getOrder()).get().getRecipeCourseSeq();
        recipeCourseRepository.deleteByRecipeCourseSeq(recipeCourseDeleteDto.getRecipeCourseSeq());
//        RecipeCourse currentRecipeCourse = recipeCourseRepository.findByRecipeCourseSeq(seq);
//        currentRecipeCourse.setUseYN('N');
//        List<RecipeCourse> recipeCourses = recipeCourseRepository.findAllByRecipe(a);
//        for(int i=recipeCourseDeleteDto.getOrder()-1; i<recipeCourses.size(); i++){
//            int orders = recipeCourses.get(i).getRecipeOrder();
//            recipeCourses.get(i).setRecipeOrder(orders-1);
//        }
        return response.success(recipeCourseDeleteDto.getRecipeSeq()+"번 레시피 특정 과정 삭제");
    }




    /**
     * 글상세 로직
     */
    @Transactional
    public Map<String, Object> viewRecipeDetail(RecipeDto.recipeDetail recipeDetailDto) {
        if (!recipeRepository.existsById(recipeDetailDto.getRecipeSeq())) { return returnRst(new HashMap<>(),"해당 레시피를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeSeq(recipeDetailDto.getRecipeSeq()).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return returnRst(new HashMap<>(),"이미 삭제된 레시피입니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("현재 선택된 레시피는 " + recipe.getRecipeSeq() + "번입니다.");

        Users user = recipe.getUser();
        String userEmail = user.getEmail();
        Users users = userRepository.findByEmail(userEmail).orElseThrow();
        String userName = userRepository.findById(users.getUserSeq()).get().getName();
        RecipeVo.recipeDetail recipeDetail = RecipeVo.recipeDetail.builder()
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

        Map<String, Object> data = new HashMap<>();
        data.put("recipeDetail", recipeDetail);

        return returnRst(data, "레시피 상세보기", HttpStatus.OK);
    }
    //레시피재료리스트상세조회
    @Transactional
    public Map<String, Object> viewRecipeIngredientDetail(RecipeDto.recipeDetail recipeIngredientDetailDto) {
        if (!recipeRepository.existsById(recipeIngredientDetailDto.getRecipeSeq())) {  return returnRst(new HashMap<>(),"레시피를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeSeq(recipeIngredientDetailDto.getRecipeSeq()).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return returnRst(new HashMap<>(),"이미 삭제된 레시피입니다.", HttpStatus.BAD_REQUEST);
        }
        List<RecipeIngredient> recipeIngredient = recipeIngredientRepository.findAllByRecipe(recipe);
        List<RecipeVo.recipeIngredientDetail> recipeIngredientList = new ArrayList<>();
        log.info("총 선택된 레시피재료는 " + recipeIngredient.size() + "개입니다.");
        for(RecipeIngredient recipeIngredients : recipeIngredient){
            RecipeVo.recipeIngredientDetail detailList = RecipeVo.recipeIngredientDetail.builder()
                    .recipeIngredientSeq(recipeIngredients.getRecipeIngredientSeq())
                    .recipeSeq(recipeIngredients.getRecipe().getRecipeSeq())
                    .ingredientName(recipeIngredients.getIngredient().getIngredientName())
                    .ingredientVolume(recipeIngredients.getVolume())
                    .build();
            recipeIngredientList.add(detailList);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("recipeIngredientList", recipeIngredientList);

        return returnRst(data, recipeIngredientDetailDto.getRecipeSeq() + "번 레시피 조회", HttpStatus.OK);
    }
    @Transactional
    public Map<String, Object> viewRecipeCourseDetail(RecipeDto.recipeDetail recipeCourseDetailDto) {
        if (!recipeRepository.existsById(recipeCourseDetailDto.getRecipeSeq())) { return returnRst(new HashMap<>(),"레시피를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST); }
        Recipe recipe = recipeRepository.findByRecipeSeq(recipeCourseDetailDto.getRecipeSeq()).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return returnRst(new HashMap<>(),"이미 삭제된 레시피입니다.", HttpStatus.BAD_REQUEST);
        }
        List<RecipeCourse> recipeCourse = recipeCourseRepository.findAllByRecipe(recipe);
        List<RecipeVo.recipeCourseDetail> recipeCourseList = new ArrayList<>();
        log.info("총 선택된 레시피 과정은 " + recipeCourse.size() + "번입니다.");

        for(RecipeCourse recipeCourses : recipeCourse){
            RecipeVo.recipeCourseDetail detailList = RecipeVo.recipeCourseDetail.builder()
                    .recipeCourseSeq(recipeCourses.getRecipeCourseSeq())
                    .recipeSeq(recipeCourses.getRecipe().getRecipeSeq())
                    .recipeOrder(recipeCourses.getRecipeOrder())
                    .courseContents(recipeCourses.getContents())
                    .fileId(recipeCourses.getFileId())
                    .tips(recipeCourses.getTips())
                    .build();
            recipeCourseList.add(detailList);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("recipeCourseList", recipeCourseList);

        return returnRst(data, recipeCourseDetailDto.getRecipeSeq() + "번 레시피 조회", HttpStatus.OK);
    }

    @Transactional
    public Map<String, Object> viewRecipeReply(RecipeDto.recipeDetail recipeReplyDto) {
        if (!recipeRepository.existsById(recipeReplyDto.getRecipeSeq())) { return returnRst(new HashMap<>(),"레시피를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);  }
        Recipe recipe = recipeRepository.findByRecipeSeq(recipeReplyDto.getRecipeSeq()).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return returnRst(new HashMap<>(),"이미 삭제된 레시피입니다.", HttpStatus.BAD_REQUEST);
        }
        List<Reply> recipeReply = replyRepository.findAllByRecipe(recipe);
        List<RecipeVo.recipeReply> replyList = new ArrayList<>();
        log.info("총 선택된 레시피 댓글은 " + recipeReply.size() + "번입니다.");

        for(Reply reply : recipeReply){
            Users user = reply.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getUserSeq()).get().getName();
            RecipeVo.recipeReply detailList = RecipeVo.recipeReply.builder()
                    .replySeq(reply.getReplySeq())
                    .recipeSeq(reply.getRecipe().getRecipeSeq())
                    .date(reply.getModifiedDate())
                    .name(userName)
                    .contents(reply.getContents())
                    .build();
            replyList.add(detailList);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("replyList", replyList);

        return returnRst(data, recipeReplyDto.getRecipeSeq() + "번 레시피 조회", HttpStatus.OK);

    }

    /**
     * 필터
     */
    @Transactional
    public ResponseEntity<?> filterRecipes(Pageable pageable, String filter) {
        Page<Recipe> recipes = recipeRepository.findByUseYNAndFoodClassTypeCode('Y', filter, pageable);

        List<Map<String,Object>> list = new ArrayList<>();
        for(Recipe recipe : recipes){
            Users user = recipe.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getUserSeq()).get().getName();

            Map<String,Object> data = new HashMap<>();
            data.put("recipeId", recipe.getRecipeSeq());
            data.put("file", recipe.getFile());
            data.put("title", recipe.getTitle());

            data.put("count", recipe.getViewCnt());
            log.debug("count :: data :: [{}]", recipe.getViewCnt());
            data.put("name", userName);

            list.add(data);
        }


        int cnt = list.size();
        return response.success(list, "filterRecipes :: "+cnt+"건 조회되었습니다!", HttpStatus.OK);
    }
    @Transactional
    public Page<Recipe> findByUseYNAndFoodClassTypeCode(Character useYN, String filter, Pageable pageable) {
        return recipeRepository.findByUseYNAndFoodClassTypeCode(useYN, filter, pageable);
    }

    /**
     * 검색(제목, useYN)
     */
    @Transactional
    public Page<Recipe> findByUseYNAndTitleContaining(Character useYN, String title, Pageable pageable) {
        return recipeRepository.findByUseYNAndTitleContaining(useYN, title, pageable);
    }

    @Transactional
    public Page<FavoriteRecipe> findByUseYNAndUser(Character useYN, Authentication authentication, Pageable pageable) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        return favoriteRecipeRepository.findByUseYNAndUser(useYN, users, pageable);
    }

    @Transactional
    public ResponseEntity<?> searchRecipes(Pageable pageable, String search) {


        Page<Recipe> recipes = recipeRepository.findByUseYNAndTitleContaining('Y', search, pageable);
        List<RecipeDto.recipeSimpleDto> data = new ArrayList<>();

        for(Recipe recipe : recipes){
            Users user = recipe.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getUserSeq()).get().getName();
            RecipeDto.recipeSimpleDto recipeLists = RecipeDto.recipeSimpleDto.builder()
                    .file(recipe.getFile())
                    .title(recipe.getTitle())
                    .count(recipe.getViewCnt())
                    .name(userName)
                    .build();
            data.add(recipeLists);
        }
        return response.success(data, "전체 레시피가 조회되었습니다!", HttpStatus.OK);
    }
    /**
     * 즐겨찾기한 레시피
     */
    @Transactional
    public ResponseEntity<?> favoritedRecipes(Pageable pageable, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        List<FavoriteRecipe> recipes = favoriteRecipeRepository.findAllByUseYNAndUser('Y', users);
//        Page<recipe> recipes = recipeRepository.findByUseYNAndTitleContaining('Y', search, pageable);
        List<RecipeDto.recipeSimpleDto> data = new ArrayList<>();
        for(FavoriteRecipe favoritedrecipe : recipes){
            Users user = favoritedrecipe.getUser();
            String userEmail = user.getEmail();
            Users writeUser = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(writeUser.getUserSeq()).get().getName();
            RecipeDto.recipeSimpleDto recipeLists = RecipeDto.recipeSimpleDto.builder()
                    .file(favoritedrecipe.getRecipe().getFile())
                    .title(favoritedrecipe.getRecipe().getTitle())
                    .count(favoritedrecipe.getRecipe().getViewCnt())
                    .name(userName)
                    .build();
            data.add(recipeLists);
        }
        return response.success(data, "즐겨찾기한 레시피가 조회되었습니다!", HttpStatus.OK);
    }

    /**
     * 즐겨찾기하기
     */
    @Transactional
    public ResponseEntity favoritedRegisterRecipe(Authentication authentication, RecipeDto.recipeFavoritedRegister recipeFavoritedRegisterDto) {
        if(!recipeRepository.existsByRecipeSeq(recipeFavoritedRegisterDto.getRecipeSeq())){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Recipe recipe = recipeRepository.findByRecipeSeq(recipeFavoritedRegisterDto.getRecipeSeq()).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 게시물이 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Recipe currentRecipe = recipeRepository.findByRecipeSeq(recipeFavoritedRegisterDto.getRecipeSeq()).orElseThrow();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();

        if(favoriteRecipeRepository.findByRecipeAndUser(currentRecipe, users) == null) {
            // 즐겨찾기를 누른적 없다면 Favorite 생성 후, 즐겨찾기 처리
            currentRecipe.setFavorite(currentRecipe.getFavorite() + 1);
            FavoriteRecipe favorite = new FavoriteRecipe(currentRecipe, users); // true 처리
            favoriteRecipeRepository.save(favorite);
            return response.success("즐겨찾기 처리 완료");
        } else {
            // 즐겨찾기 누른적 있다면 즐겨찾기 처리 후 테이블 삭제
            FavoriteRecipe favorite = favoriteRecipeRepository.findFavoriteByRecipe(currentRecipe);
            currentRecipe.setFavorite(currentRecipe.getFavorite() - 1);
            favoriteRecipeRepository.delete(favorite);
            return response.success("즐겨찾기 취소 완료");
        }
    }

    /**
     * 글 조회수 로직
     */
    @Transactional
    public int updateCount(int id) {
        return recipeRepository.updateCount(id);
    }

    /**
     * 글목록 로직
     */
    @Transactional
    public Page<Recipe> findByUseYN(Character useYN, Pageable pageable) {
        return recipeRepository.findByUseYN(useYN, pageable);
    }
    @Transactional
    public ResponseEntity<?> mainRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findByUseYN('Y', pageable);
        List<RecipeDto.recipeSimpleDto> data = new ArrayList<>();

        for(Recipe recipe : recipes){
            Users user = recipe.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getUserSeq()).get().getName();
            RecipeDto.recipeSimpleDto recipeLists = RecipeDto.recipeSimpleDto.builder()
                    .file(recipe.getFile())
                    .title(recipe.getTitle())
                    .count(recipe.getViewCnt())
                    .name(userName)
                    .contents(recipe.getContents())
                    .build();
            data.add(recipeLists);
        }
        return response.success(data, "전체 레시피가 조회되었습니다!", HttpStatus.OK);
    }





}