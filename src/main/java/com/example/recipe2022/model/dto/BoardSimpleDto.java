package com.example.recipe2022.model.dto;

import com.example.recipe2022.model.data.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleDto {
    private int id;
    private String title;
    private String nickname;
    private int liked;
    private int favorited;

    public BoardSimpleDto toDto(Board board) {
        return new BoardSimpleDto(board.getId(), board.getTitle(), board.getUser().getName(), board.getRecommend(),
                board.getFavorited());
    }
}