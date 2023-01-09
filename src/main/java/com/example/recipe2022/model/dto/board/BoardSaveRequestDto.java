package com.example.recipe2022.model.dto.board;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardSaveRequestDto {

    private String title;
    private String content;
    private int count;
    private User user;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .view(0)
                .user(user)
                .build();
    }

    public void setUser(User user) {
        this.user = user;
    }
}