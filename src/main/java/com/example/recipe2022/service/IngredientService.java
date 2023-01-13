package com.example.recipe2022.service;

import com.example.recipe2022.model.data.Ingredient;
import com.example.recipe2022.model.repository.IngredientRepository;
import com.example.recipe2022.model.vo.IngredientVo;
import com.example.recipe2022.model.vo.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IngredientService {
    private final Response response;
    private final IngredientRepository ingredientRepository;
    public ResponseEntity<?> searchIngredient(String name) {
        List<Ingredient> ingredientList = ingredientRepository.findByIngredientNameContaining(name);
        log.info("검색 결과는 " + ingredientList.size() + "개 입니다.");

        List<IngredientVo.search> data = new ArrayList<>();
        for (Ingredient ingredient : ingredientList) {
            IngredientVo.search detailList = IngredientVo.search.builder()
                    .ingredientName(ingredient.getIngredientName())
                    .ingredientType(ingredient.getIngredientType())
                    .build();
            data.add(detailList);
        }
        return response.success(data,"검색 결과 [" + data.size() + "]개 입니다.", HttpStatus.OK);
    }
}