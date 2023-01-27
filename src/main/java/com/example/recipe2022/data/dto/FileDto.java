package com.example.recipe2022.data.dto;

import lombok.*;

import java.time.LocalDateTime;

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
        private LocalDateTime date;
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