package com.example.recipe2022.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class ReplyDto {
    @Getter
    @Setter
    @Builder
    public static class replyCreate{
        @NotNull
        private String replyContents;

    }

}
