package com.example.recipe2022.data.dto;

import com.example.recipe2022.data.entity.Ingredient;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class RecipeDto {

    @Getter
    @Setter
    @Builder
    public static class recipeCreate{
        @NotNull
        private String recipeTitle;
        @NotNull
        private String recipeContents;
        private String recipeFile;
        @NotNull
        private String foodClassName;
        @NotNull
        private String recipeVolume;
        @NotNull
        private String recipeTime;
        @NotNull
        private int recipeLevel;

    }
    public static class putIngredient {
        @NotNull(message = "재료를 추가해주세요.")
        private Ingredient ingredient;
    }
    public static class ingredientCreate {
        @NotNull(message = "재료를 추가해주세요.")
        private String name;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeCourseCreate{
        @NotNull(message = "내용을 입력해주세요.")
        private String contents;
        private String recipeFile;
        private String tips;
    }

    @Getter
    @Setter
    @Builder
    public static class recipeIngredientCreate{
        private String volume;
    }
}