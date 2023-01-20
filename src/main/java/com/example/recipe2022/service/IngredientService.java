package com.example.recipe2022.service;

import com.example.recipe2022.data.entity.Ingredient;
import com.example.recipe2022.repository.IngredientRepository;
import com.example.recipe2022.repository.UserRepository;
import com.example.recipe2022.data.dto.IngredientDto;
import com.example.recipe2022.data.dao.Response;
import io.swagger.models.auth.In;
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
    private final UserRepository userRepository;
    public ResponseEntity<?> searchIngredient(IngredientDto.searchName search) {
        if (ingredientRepository.findByIngredientNameContaining(search.getIngredientName()).isEmpty()) {
            return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        List<Ingredient> ingredientList = ingredientRepository.findByIngredientNameContaining(search.getIngredientName());
        log.info("검색 결과는 " + ingredientList.size() + "개 입니다.");

        List<IngredientDto.search> data = new ArrayList<>();
        for (Ingredient ingredient : ingredientList) {
            IngredientDto.search detailList = IngredientDto.search.builder()
                    .ingredientName(ingredient.getIngredientName())
                    .ingredientType(ingredient.getIngredientType())
                    .build();
            data.add(detailList);
        }
        return response.success(data,"검색 결과 [" + data.size() + "]개 입니다.", HttpStatus.OK);
    }
}