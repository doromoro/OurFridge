package com.example.recipe2022.view;

import com.example.recipe2022.service.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping("/ingredient/search")
    public ResponseEntity<?> searchIngredient(String name) {
        return ingredientService.searchIngredient(name);
    }
}