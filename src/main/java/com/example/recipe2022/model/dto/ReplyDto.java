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
        private int boardSeq;
        @NotNull
        private String replyContents;

    }
    @Getter
    @Setter
    @Builder
    public static class replyDelete{
        private int replySeq;
    }

}
