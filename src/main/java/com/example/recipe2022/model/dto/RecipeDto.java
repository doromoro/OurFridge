package com.example.recipe2022.model.dto;

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
        private int recipeFoodCode;
        @NotNull
        private String recipeVolume;
        @NotNull
        private String recipeTime;
        @NotNull
        private int recipeLevel;
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