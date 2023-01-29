package com.example.recipe2022.data.dto;

import com.example.recipe2022.data.entity.Ingredient;
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

        private List<recipeIngredientCreate> recipeIngredientList;
        private List<recipeCourseCreate> recipeCourseList;

        private int recipeSeq;


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

        // 수정 관련 배열
        private List<recipeIngredientUpdate> recipeIngredientList;
        private List<recipeCourseUpdate> recipeCourseList;

        // 삭제 관련 배열
        private List<recipeIngredientUpdate> recipeIngredientDeleteList;
        private List<recipeCourseUpdate> recipeCourseDeleteList;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientUpdate{
        private int recipeIngredientSeq;
        private int ingSeq;
        private int recipeSeq;
        private String volume;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeCourseUpdate{
        private int recipeCourseSeq;
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
        private List<recipeIngredientDelete> recipeIngredientList;
        private List<recipeCourseDelete> recipeCourseList;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeIngredientDelete{
        private int recipeIngSeq;
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
        private List<recipeIngredientDetail> recipeIngredientList;
        private List<recipeCourseDetail> recipeCourseList;
        private List<recipeReply> recipeReplyList;
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

    @Getter
    @Setter
    @Builder
    public static class recipeSimpleDto{
        private String file;
        private String title;
        private int count;
        private String name;
        private String contents;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeFavoritedRegister{
        private int recipeSeq;
    }

    // recipe dto
    @Getter
    @Setter
    @Builder
    public static class recipe{
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

        private List<recipeIngredient> recipeIngredientList;
        private List<recipeCourse> recipeCourseList;

        // 삭제 관련 배열
        private List<recipeIngredient> recipeIngredientDeleteList;
        private List<recipeCourse> recipeCourseDeleteList;
    }

    @Getter
    @Setter
    @Builder
    public static class recipeIngredient{
        private int recipeIngredientSeq;
        private int ingSeq;
        private int recipeSeq;

        private String volume;
    }
    @Getter
    @Setter
    @Builder
    public static class recipeCourse{
        private int recipeCourseSeq;
        private int recipeSeq;

        @NotNull(message = "내용을 입력해주세요.")
        private String contents;
        private int order;
        private String recipeFile;
        private String tips;
    }

}