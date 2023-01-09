package com.example.recipe2022.model.vo;

import com.example.recipe2022.model.data.Fridge;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


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
    public static class myFridgeList {
        private List<Fridge> fridgeList;
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


}
