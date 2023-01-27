package com.example.recipe2022.data.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class recipeDto {
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

}