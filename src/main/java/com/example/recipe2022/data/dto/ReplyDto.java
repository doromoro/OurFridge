package com.example.recipe2022.data.dto;
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
        private int recipeSeq;
        @NotNull
        private String replyContents;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class replyDelete{
        private int replySeq;
    }

}