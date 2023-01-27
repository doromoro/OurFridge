package com.example.recipe2022.data.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class FileDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class updateUserPicture{
        private String username;
        private String nums;
        private String date;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class updateRecipePicture{
        private int recipeSeq;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class updateCoursePicture{
        private int courseSeq;
    }
}