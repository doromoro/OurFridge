package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.recipeDto;
import com.example.recipe2022.data.entity.recipe;
import com.example.recipe2022.data.entity.Favoriterecipe;
import com.example.recipe2022.data.entity.Users;
import com.example.recipe2022.repository.recipeRepository;
import com.example.recipe2022.repository.FavoriterecipeRepository;
import com.example.recipe2022.repository.UserRepository;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class recipeService {

    private final recipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final Response response;
    private final FavoriterecipeRepository favoriterecipeRepository;

    /**
     * 검색(제목, useYN)
     */
    @Transactional
    public Page<recipe> findByUseYNAndTitleContaining(Character useYN, String title, Pageable pageable) {
        return recipeRepository.findByUseYNAndTitleContaining(useYN, title, pageable);
    }

    @Transactional
    public Page<Favoriterecipe> findByUseYNAndUser(Character useYN, Authentication authentication, Pageable pageable) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        return favoriterecipeRepository.findByUseYNAndUser(useYN, users, pageable);
    }

    @Transactional
    public ResponseEntity<?> searchrecipes(Pageable pageable, String search) {


        Page<recipe> recipes = recipeRepository.findByUseYNAndTitleContaining('Y', search, pageable);
        List<recipeDto.recipeSimpleDto> data = new ArrayList<>();

        for(recipe recipe : recipes){
            Users user = recipe.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getId()).get().getName();
            recipeDto.recipeSimpleDto recipeLists = recipeDto.recipeSimpleDto.builder()
                    .file(recipe.getFileId())
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
    public ResponseEntity<?> favoritedrecipes(Pageable pageable, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        List<Favoriterecipe> recipes = favoriterecipeRepository.findAllByUseYNAndUser('Y', users);
        List<recipeDto.recipeSimpleDto> data = new ArrayList<>();
        for(Favoriterecipe favoritedrecipe : recipes){
            Users user = favoritedrecipe.getUser();
            String userEmail = user.getEmail();
            Users writeUser = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(writeUser.getId()).get().getName();
            recipeDto.recipeSimpleDto recipeLists = recipeDto.recipeSimpleDto.builder()
                    .file(favoritedrecipe.getrecipe().getFileId())
                    .title(favoritedrecipe.getrecipe().getTitle())
                    .count(favoritedrecipe.getrecipe().getViewCnt())
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
    public ResponseEntity favoritedRegisterRecipe(Authentication authentication, int recipeSeq) {
        if(!recipeRepository.existsById(recipeSeq)){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        recipe recipe = recipeRepository.findById(recipeSeq).orElseThrow();
        if(recipe.getUseYN() == 'N'){
            return response.fail("해당 게시물이 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        recipe currentrecipe = recipeRepository.findById(recipeSeq).orElseThrow();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();

        if(favoriterecipeRepository.findByrecipeAndUser(currentrecipe, users) == null) {
            // 즐겨찾기를 누른적 없다면 Favorite 생성 후, 즐겨찾기 처리
            currentrecipe.setFavorite(currentrecipe.getFavorite() + 1);
            Favoriterecipe favorite = new Favoriterecipe(currentrecipe, users); // true 처리
            favoriterecipeRepository.save(favorite);
            return response.success("즐겨찾기 처리 완료");
        } else {
            // 즐겨찾기 누른적 있다면 즐겨찾기 처리 후 테이블 삭제
            Favoriterecipe favorite = favoriterecipeRepository.findFavoriteByrecipe(currentrecipe);
            currentrecipe.setFavorite(currentrecipe.getFavorite() - 1);
            favoriterecipeRepository.delete(favorite);
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
    public Page<recipe> findByUseYN(Character useYN, Pageable pageable) {
        return recipeRepository.findByUseYN(useYN, pageable);
    }
    @Transactional
    public ResponseEntity<?> mainrecipes(Pageable pageable) {
        Page<recipe> recipes = recipeRepository.findByUseYN('Y', pageable);
        List<recipeDto.recipeSimpleDto> data = new ArrayList<>();

        for(recipe recipe : recipes){
            Users user = recipe.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getId()).get().getName();
            recipeDto.recipeSimpleDto recipeLists = recipeDto.recipeSimpleDto.builder()
                    .file(recipe.getFileId())
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