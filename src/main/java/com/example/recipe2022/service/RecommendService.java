package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.FridgeDto;
import com.example.recipe2022.data.dto.RecommendDto;
import com.example.recipe2022.data.entity.Fridge;
import com.example.recipe2022.data.entity.FridgeIngredient;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.data.entity.RecipeIngredient;
import com.example.recipe2022.repository.FridgeIngredientRepository;
import com.example.recipe2022.repository.FridgeRepository;
import com.example.recipe2022.repository.RecipeIngredientRepository;
import com.example.recipe2022.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final int[] weight = {10, 5, 2};
    public ResponseEntity<?> recommend(FridgeDto.fridgeSequence fridgeSeq) {
        if (fridgeRepository.findByFridgeId(fridgeSeq.getFridgeSeq()).orElse(null) == null) {
            return response.fail("냉장고가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Fridge fridge = fridgeRepository.findByFridgeId(fridgeSeq.getFridgeSeq()).orElseThrow();
        if (fridgeIngredientRepository.countByFridge(fridge) == 0) {return response.fail("냉장고에 재료가 하나도 없어요.", HttpStatus.BAD_REQUEST);}
        List<FridgeIngredient> fridgeIngredient = fridgeIngredientRepository.findAllByFridge(fridge);
        List<Recipe> allRecipe = recipeRepository.findAll();
        List<RecommendDto> data = new ArrayList<>();

        for (Recipe recipes : allRecipe) {
            String recName = recipes.getTitle();
            int recSum = 0;
            List<String> insufficients = new ArrayList<>();
            List<String> sufficients = new ArrayList<>();
            List<String> resultLists = new ArrayList<>();
            List<RecipeIngredient> recipeIngredient = recipeIngredientRepository.findAllByRecipe(recipes);
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
                        if (!sufficients.contains(ingredient.getIngredient().getIngredientName())) {
                            sufficients.add(ingredient.getIngredient().getIngredientName());
                        }
                    }
                    else {
                        if (!insufficients.contains(ingredient.getIngredient().getIngredientName())) {
                            insufficients.add(ingredient.getIngredient().getIngredientName());
                        }
                    }
                }
                resultLists = insufficients.stream()
                        .filter(old -> sufficients.stream().noneMatch(Predicate.isEqual(old)))
                        .collect(Collectors.toList());
            }
            if (recSum != 0) {
                RecommendDto yes = RecommendDto.builder()
                        .insufficientList(resultLists)
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