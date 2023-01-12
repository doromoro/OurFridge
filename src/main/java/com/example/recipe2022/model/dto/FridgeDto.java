package com.example.recipe2022.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class FridgeDto {

        @Getter
        @Setter
        @Builder
        public static class fridgeCreate {
                @NotNull(message = "냉장고 이름을 입력해주세요")
                private String fridgeName;
                @NotNull(message = "냉장고 설명을 입력해주세요")
                private String fridgeDetail;
        }
}