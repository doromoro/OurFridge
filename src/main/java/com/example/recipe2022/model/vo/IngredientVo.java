package com.example.recipe2022.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class IngredientVo {

    @Getter
    @Setter
    @Builder
    public static class search {
        private String ingredientName;
        private String ingredientType;
    }
}