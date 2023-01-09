package com.example.recipe2022.service;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.User;
import com.example.recipe2022.model.dto.board.BoardSaveRequestDto;
import com.example.recipe2022.model.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

//    @Transactional
//    public int save(BoardSaveRequestDto boardSaveRequestDto, User user) {
//        boardSaveRequestDto.setUser(user);
//        return boardRepository.save(boardSaveRequestDto.toEntity()).getId();
//    }

    @Transactional
    public void 글쓰기(Board board, User user){
        board.setView(0);
        board.setUser(user);
        boardRepository.save(board);
    }

}
