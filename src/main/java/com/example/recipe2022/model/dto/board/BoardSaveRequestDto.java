package com.example.recipe2022.model.dto.board;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.User;
import lombok.*;

import javax.persistence.Entity;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveRequestDto {
    private String title;
    private String contents;
    private String file;
    private int foodCode;
    private String volume;
    private String time;
    private int level;



//    public Board toEntity() {
//        System.out.println("a");
//        return Board.builder()
//                .id(1)
//                .title(title)
//                .content(content)
//                .view(0)
//                .user(user)
//                .build();
//    }



//    public void setUser(User user) {
//        this.user = user;
//    }

}