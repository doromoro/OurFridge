package com.example.recipe2022.model.dto;

import com.example.recipe2022.model.data.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleDto {
    private String file;
    private String title;
    private int count;
    private String name;

    public BoardSimpleDto toDto(Board board) {
        return new BoardSimpleDto(board.getFile_grp_id(), board.getTitle(), board.getView(), board.getUser().getName());
    }
}