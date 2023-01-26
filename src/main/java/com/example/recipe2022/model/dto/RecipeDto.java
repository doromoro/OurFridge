package com.example.recipe2022.model.dto;

import com.example.recipe2022.model.data.Ingredient;
import com.example.recipe2022.model.data.Recipe;
import com.example.recipe2022.model.data.RecipeIngredient;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

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
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientCreate{
        private int ingSeq;
        private int recipeSeq;
        private String volume;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeCourseCreate{
        private int recipeSeq;
        @NotNull(message = "내용을 입력해주세요.")
        private String contents;
        private String recipeFile;
        private String tips;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeUpdate{
        int recipeSeq;
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
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientUpdate{
        private int ingSeq;
        private int recipeSeq;
        private String volume;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeCourseUpdate{
        private int order;
        private int recipeSeq;
        @NotNull(message = "내용을 입력해주세요.")
        private String contents;
        private String recipeFile;
        private String tips;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeDelete{
        private int recipeSeq;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientDelete{
        private int ingSeq;
        private int recipeSeq;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeCourseDelete{
        private int order;
        private int recipeSeq;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeDetail{
        private int recipeSeq;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientDetail{
        private int recipeSeq;
    }

    @Getter
    @Setter
    @Builder
    public static class recipeCourseDetail{
        private int recipeSeq;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeReply{
        private int recipeSeq;
    }











}