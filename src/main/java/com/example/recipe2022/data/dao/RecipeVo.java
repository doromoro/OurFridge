package com.example.recipe2022.data.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class RecipeVo {
    @Getter
    @Setter
    @Builder
    public static class recipeDetail{
        private LocalDateTime date;
        private String userName;
        private String title;
        private String contents;
        private String file;
        private String foodClassName;
        private String volume;
        private String time;
        private int level;

    }
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientDetail{
        private String ingredientName;
        private String ingredientVolume;
    }

    @Getter
    @Setter
    @Builder
    public static class recipeCourseDetail{
        private int recipeOrder;
        private String courseContents;
        private String fileId;
        private String tips;
    }

    @Getter
    @Setter
    @Builder
    public static class recipeReply{
        private LocalDateTime date;
        private String contents;
        private String name;

    }
}