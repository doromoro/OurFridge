package com.example.recipe2022.model.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class BoardDto {
    @Getter
    @Setter
    @Builder
    public static class boardSimpleDto{
        private String file;
        private String title;
        private int count;
        private String name;
        private String contents;
    }
    @Getter
    @Setter
    @Builder
    public static class boardFavoritedRegister{
        private int boardSeq;
    }

}
