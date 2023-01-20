package com.example.recipe2022.model.dto;

import com.example.recipe2022.model.data.Ingredient;
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
        private int recipeFoodCode;
        @NotNull
        private String recipeVolume;
        @NotNull
        private String recipeTime;
        @NotNull
        private int recipeLevel;

//        private List<RecipeIngredient> recipeIngredient;
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
        @NotNull
        private String contents;
        private String recipeFile;
        private String tips;
    }




//    public Board toEntity() {
//        System.out.println("a");
//        return Board.builder()
//                .id(1)
//                .title(title)
//                .content(content)
//                .view(0)
//                .user(user)
//                .build();
//    }



//    public void setUser(User user) {
//        this.user = user;
//    }

}