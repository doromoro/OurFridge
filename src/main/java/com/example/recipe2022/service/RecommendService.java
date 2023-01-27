package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.FridgeDto;
import com.example.recipe2022.data.dto.RecommendDto;
import com.example.recipe2022.data.entity.*;
import com.example.recipe2022.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendService {
    private final RecipeRepository recipeRepository;
    private final FridgeRepository fridgeRepository;
    private final Response response;
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    private final int[] weight = {10, 5, 2};
    public ResponseEntity<?> recommendRecipe(Authentication authentication, FridgeDto.searchFridge fridgeSeq) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        if (userRepository.findByEmail(email).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Users users = userRepository.findByEmail(email).orElseThrow();
        if (fridgeRepository.findByFridgeId(fridgeSeq.getFridgeSeq()).orElse(null) == null) {
            return response.fail("냉장고가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Fridge fridge;
        fridge = fridgeRepository.findByFridgeId(fridgeSeq.getFridgeSeq()).orElseThrow();
        if (!users.getFridges().contains(fridge)) {
            return response.failBadGate();
        }
        if (fridgeIngredientRepository.countByFridge(fridge) == 0) {return response.fail("냉장고에 재료가 하나도 없어요.", HttpStatus.BAD_REQUEST);}
        List<FridgeIngredient> fridgeIngredient;
        fridgeIngredient = fridgeIngredientRepository.findAllByFridge(fridge);
        List<Recipe> allRecipe;
        allRecipe = recipeRepository.findAll();
        List<RecommendDto> data;
        data = new ArrayList<>();

        for (Recipe recipes : allRecipe) {
            String recName;
            recName = recipes.getTitle();
            int recSum = 0;
            List<Integer> insufficients = new ArrayList<>();
            List<Integer> sufficients = new ArrayList<>();
            List<Integer> resultLists = new ArrayList<>();
            List<RecommendDto.insufficientIngredient> dtoList = new ArrayList<>();
            List<RecipeIngredient> recipeIngredient;
            recipeIngredient = recipeIngredientRepository.findAllByRecipe(recipes);
            for (RecipeIngredient recipe : recipeIngredient) {
                int ingSeq = recipe.getIngredient().getIngredientId();
                int ingType = recipe.getIngredient().getIngredientTypeCode();
                for (FridgeIngredient ingredient : fridgeIngredient) {
                    if (ingSeq == ingredient.getIngredient().getIngredientId()) {
                        switch (ingType) {
                            case 3060001:
                                recSum += weight[0];    //주재료
                                break;
                            case 3060002:
                                recSum += weight[1];    //부재료
                                break;
                            case 3060003:
                                recSum += weight[2];     //양념
                                break;
                            default:
                                return response.fail("알 수 없는 오류", HttpStatus.BAD_REQUEST);
                        }
                        if (!sufficients.contains(ingredient.getIngredient().getIngredientId())) {
                            sufficients.add(ingredient.getIngredient().getIngredientId());
                        }
                    }
                    else {
                        if (!insufficients.contains(ingredient.getIngredient().getIngredientId())) {
                            insufficients.add(ingredient.getIngredient().getIngredientId());
                        }
                    }
                }
            }
            resultLists = insufficients.stream()
                    .filter(old -> sufficients.stream().noneMatch(Predicate.isEqual(old)))
                    .collect(Collectors.toList());
            for (Integer resultList : resultLists) {
                String name = ingredientRepository.findByIngredientId(resultList).get().getIngredientName();
                String type = ingredientRepository.findByIngredientId(resultList).get().getIngredientType();
                RecommendDto.insufficientIngredient tmp = RecommendDto.insufficientIngredient.builder()
                        .name(name)
                        .type(type)
                        .build();
                dtoList.add(tmp);
            }
            if (recSum >= 10) {
                RecommendDto yes = RecommendDto.builder()
                        .insufficientList(dtoList)
                        .recipeName(recName)
                        .weight(recSum)
                        .build();
                data.add(yes);
            }
        }

        data.sort(Comparator.comparing(RecommendDto::getWeight, Comparator.reverseOrder()));
        return response.success(data, ("[" +fridge.getFridgeName() + "] 냉장고의 추천 레시피입니다."), HttpStatus.OK);
    }
}