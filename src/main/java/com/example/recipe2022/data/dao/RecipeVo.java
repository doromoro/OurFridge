package com.example.recipe2022.data.dao;

import com.example.recipe2022.data.dto.RecipeDto;
import com.example.recipe2022.data.entity.Files;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class RecipeVo {
    @Getter
    @Setter
    @Builder
    public static class recipeDetail{
        private LocalDateTime date;
        private String userName;
        private String title;
        private String contents;
        private Files file;
        private String foodClassName;
        private String volume;
        private String time;
        private int level;

        private List<RecipeVo.recipeIngredientDetail> recipeIngredientList;
        private List<RecipeVo.recipeCourseDetail> recipeCourseList;
        private List<RecipeVo.recipeReply> recipeReplyList;

    }
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientDetail{
        private int recipeIngredientSeq;
        private int recipeSeq;
        private String ingredientName;
        private String ingredientVolume;
    }

    @Getter
    @Setter
    @Builder
    public static class recipeCourseDetail{
        private int recipeCourseSeq;
        private int recipeSeq;
        private int recipeOrder;
        private String courseContents;
        private Files file;
        private String tips;
    }

    @Getter
    @Setter
    @Builder
    public static class recipeReply{
        private int replySeq;
        private int recipeSeq;
        private LocalDateTime date;
        private String contents;
        private String name;

    }
}