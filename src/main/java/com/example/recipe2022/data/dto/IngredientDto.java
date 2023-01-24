package com.example.recipe2022.data.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class IngredientDto {

    @Getter
    @Setter
    @Builder
    public static class ingredientResult {
        private String ingredientName;
        private String ingredientType;
    }
    @Getter
    @Setter
    public static class searchIngredient {
        private String ingredientName;
    }
}