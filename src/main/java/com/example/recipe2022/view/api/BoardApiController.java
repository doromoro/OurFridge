package com.example.recipe2022.view.api;

import com.example.recipe2022.config.auth.PrincipalDetail;
import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.dto.ResponseDto;
import com.example.recipe2022.model.dto.board.BoardSaveRequestDto;
import com.example.recipe2022.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BoardApiController {

    private BoardService boardService;

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) {
        boardService.글쓰기(board, principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

//    @PostMapping("/api/board")
//    public int save(@RequestBody BoardSaveRequestDto boardSaveRequestDto, @AuthenticationPrincipal PrincipalDetail principalDetail) {
//        return boardService.save(boardSaveRequestDto, principalDetail.getUser());
//    }
}