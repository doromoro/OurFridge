package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.IngredientDto;
import com.example.recipe2022.data.entity.Ingredient;
import com.example.recipe2022.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IngredientService {
    private final Response response;
    private final IngredientRepository ingredientRepository;
    public ResponseEntity<?> searchIngredient(IngredientDto.searchIngredient search) {
        if (ingredientRepository.findByIngredientNameContaining(search.getIngredientName()).isEmpty()) {
            return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<Ingredient> ingredientList;
        ingredientList = ingredientRepository.findByIngredientNameContaining(search.getIngredientName());
        log.info("검색 결과는 " + ingredientList.size() + "개 입니다.");

        List<IngredientDto.ingredientResult> data = new ArrayList<>();
        for (Ingredient ingredient : ingredientList) {
            IngredientDto.ingredientResult detailList = IngredientDto.ingredientResult.builder()
                    .ingredientName(ingredient.getIngredientName())
                    .ingredientType(ingredient.getIngredientType())
                    .build();
            data.add(detailList);
        }
        data.sort(Comparator.comparing(IngredientDto.ingredientResult::getIngredientName, Comparator.reverseOrder()));
        return response.success(data,"검색 결과 [" + data.size() + "]개 입니다.", HttpStatus.OK);
    }
}