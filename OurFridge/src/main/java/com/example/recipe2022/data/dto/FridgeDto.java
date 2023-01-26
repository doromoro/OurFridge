package com.example.recipe2022.data.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class FridgeDto {
        @Getter
        @Setter
        @Builder
        public static class newFridge {
                @NotNull(message = "냉장고 이름을 입력해주세요")
                private String fridgeName;
                @NotNull(message = "냉장고 설명을 입력해주세요")
                private String fridgeDetail;
        }
        @Getter
        @Setter
        @Builder
        public static class updateFridge {
                private int fridgeSeq;
                @NotNull(message = "냉장고 이름을 입력해주세요")
                private String fridgeName;
                @NotNull(message = "냉장고 설명을 입력해주세요")
                private String fridgeDetail;
        }
        @Getter
        @Setter
        public static class defaultFridge {
                private int fridgeSeq;
        }
        @Getter
        @Setter
        public static class deleteFridge{
                private int fridgeSeq;
        }
        @Getter
        @Setter
        public static class searchFridge {
                @NotNull(message = "냉장고 이름을 입력해주세요")
                private int fridgeSeq;
        }
        @Getter
        @Setter
        public static class putIngredient {
                private int fridgeSeq;
                private int ingredientId;
        }
        @Getter
        @Setter
        public static class deleteIngredient {
                private int fridgeSeq;
                private int ingredientId;
        }
        @Getter
        @Setter
        public static class viewIngredient {
                private int fridgeSeq;
        }
}