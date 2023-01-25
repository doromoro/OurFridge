package com.example.recipe2022.data.dto;

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

}