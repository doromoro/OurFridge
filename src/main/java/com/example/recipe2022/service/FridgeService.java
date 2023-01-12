package com.example.recipe2022.service;

import com.example.recipe2022.model.data.Fridge;
import com.example.recipe2022.model.data.FridgeIngredient;
import com.example.recipe2022.model.data.Ingredient;
import com.example.recipe2022.model.data.Users;
import com.example.recipe2022.model.dto.FridgeDto;
import com.example.recipe2022.model.repository.FridgeIngredientRepository;
import com.example.recipe2022.model.repository.FridgeRepository;
import com.example.recipe2022.model.repository.IngredientRepository;
import com.example.recipe2022.model.repository.UserRepository;
import com.example.recipe2022.model.vo.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.function.Supplier;


@Slf4j
@RequiredArgsConstructor
@Service
public class FridgeService {
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final IngredientRepository ingredientRepository;

    private final UserRepository userRepository;
    private final Response response;
    private final FridgeRepository fridgeRepository;
    private Supplier<? extends Throwable> ex;

    public ResponseEntity<?> createFridge(Authentication authentication, FridgeDto.fridgeCreate fridgeDto) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        long currentCount = fridgeRepository.countByUser(users);
        if (currentCount > 4) { return response.fail("냉장고는 총 5개까지 만들수 있어요.", HttpStatus.BAD_REQUEST); }
        Fridge fridge = Fridge.builder()
                .fridgeDetail(fridgeDto.getFridgeDetail())
                .fridgeFavorite(false)
                .fridgeName(fridgeDto.getFridgeName())
                .user(users)
                .build();
        fridgeRepository.save(fridge);
        return response.success( ( "[ " + currentCount + " ] 번째 냉장고가 생성되었습니다") );
        // 로그인 유저를 기반으로 냉장고 객체를 가져옴.
    }
    public ResponseEntity<?> updateFridge(int fridgeSeq, FridgeDto.fridgeCreate fridgeDto) {
        if (!fridgeRepository.existsByFridgeId(fridgeSeq)) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Fridge currentFridge = fridgeRepository.findByFridgeId(fridgeSeq).orElseThrow();
        String updateName=fridgeDto.getFridgeName();
        String updateDetail=fridgeDto.getFridgeDetail();
        currentFridge.setFridgeName(updateName);
        currentFridge.setFridgeDetail(updateDetail);
        fridgeRepository.save(currentFridge);
        return response.success( "냉장고 수정 성공" );
    }
    public ResponseEntity<?> defaultFridge(int fridgeSeq) {
        Fridge currentFridge = fridgeRepository.findByFridgeId(fridgeSeq).orElseThrow();
        if (!fridgeRepository.existsByFridgeId(fridgeSeq)) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        currentFridge.setFridgeFavorite(!currentFridge.isFridgeFavorite());
        log.info("디폴트 냉장고 수정 -> " + currentFridge.isFridgeFavorite());
        fridgeRepository.save(currentFridge);
        return response.success("성공적으로 변경되었습니다.");
    }
    public ResponseEntity<?> deleteFridge(int fridgeSeq) {
        if (!fridgeRepository.existsByFridgeId(fridgeSeq)) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        fridgeRepository.deleteByFridgeId(fridgeSeq);
        return response.success("성공적으로 삭제되었습니다.");
    }
/*
    public ResponseEntity<?> putIngredientToFridge(String name, int fridgeSeq) {

        Ingredient ingredient = ingredientRepository.findByIngredientName(name).orElseThrow();
        Fridge fridge = fridgeRepository.findByFridgeId(fridgeSeq).orElseThrow();
        for (Ingredient ingredient : ingredientList) {
            FridgeIngredient fridgeIngredient = FridgeIngredient.builder()
                    .ingredient(ingredient)
                    .fridge(fridge)
                    .build();
            fridgeIngredientRepository.save(fridgeIngredient);
        }

        log.info("start");
        Ingredient ingredient = ingredientRepository.find("쌀").orElseThrow();
        log.info("finish");

        Fridge fridge = fridgeRepository.findByFridgeId(fridgeSeq).orElseThrow();
        FridgeIngredient fridgeIngredient = FridgeIngredient.builder()
                .ingredient(ingredient)
                .fridge(fridge)
                .build();
        fridgeIngredientRepository.save(fridgeIngredient);
        return response.success("성공적으로 추가되었습니다.");
    }*/
}