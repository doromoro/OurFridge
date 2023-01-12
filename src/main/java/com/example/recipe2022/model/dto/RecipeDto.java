package com.example.recipe2022.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {

    @Getter
    @Setter
    @Builder
    public static class recipeCreate{
        @NotNull
        private String recipeTitle;
        private String recipeContents;
        private String recipeFile;
        private int recipeFoodCode;
        private String recipeVolume;
        private String recipeTime;
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