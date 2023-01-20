package com.example.recipe2022.data.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class IngredientDto {

    @Getter
    @Setter
    @Builder
    public static class search {
        private String ingredientName;
        private String ingredientType;
    }

    @Getter
    @Setter
    @Builder
    public static class searchName {
        private String ingredientName;
    }

    @Getter
    @Setter
    @Builder
    public static class put {
        private String ingredientName;
    }
}