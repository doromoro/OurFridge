package com.example.recipe2022.data.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class MyPageVo {
    @Getter
    @Setter
    @Builder
    public static class myPage {
        private String userEmail;
        private String userName;
        private LocalDateTime userDate;
        private String userNums;
    }
    @Getter
    @Setter
    @Builder
    public static class pwReset {
        private String email;
    }

    @Getter
    @Setter
    @Builder
    public static class myFridgeDetail {
        private int fridgeSeq;
        private String fridgeDetail;
        private String fridgeName;
        private boolean fridgeFavorite;
    }

    @Getter
    @Setter
    @Builder
    public static class myFridgeIngredientDetail {
        private String ingredientName;
        private String ingredientType;
    }
}