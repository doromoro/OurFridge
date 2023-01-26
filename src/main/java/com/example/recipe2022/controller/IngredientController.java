package com.example.recipe2022.controller;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.IngredientDto;
import com.example.recipe2022.service.IngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "재료 관련 기능")
public class IngredientController {
    private final IngredientService ingredientService;

    private final Response response;
    @GetMapping("/ingredient/search")
    @ApiOperation(value = "재료 검색", notes = "재료 이름을 가지고 검색")
    public ResponseEntity<?> searchIngredient(
            @ApiParam("재료 이름")
            @RequestBody
            IngredientDto.searchIngredient search) {
        return ingredientService.searchIngredient(search);
    }
}