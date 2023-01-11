package com.example.recipe2022.view.api;

import com.example.recipe2022.config.auth.PrincipalDetail;
import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.dto.ResponseDto;
import com.example.recipe2022.model.dto.board.BoardSaveRequestDto;
import com.example.recipe2022.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardApiController {
    @Autowired
    private BoardService boardService;

//    @PostMapping("/api/board")
//    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
//        boardService.save(board, principal.getUser());
//        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//    }

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody Board board) {
        boardService.save(board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}