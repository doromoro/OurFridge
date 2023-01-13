package com.example.recipe2022.view;

import com.example.recipe2022.model.data.Reply;
import com.example.recipe2022.model.dto.FridgeDto;
import com.example.recipe2022.model.dto.ReplyDto;
import com.example.recipe2022.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글 작성 API
     */
    @PostMapping("/recipe/{boardId}/reply")
    @ApiOperation(value = "댓글 등록")
    public ResponseEntity<?> createReply(@PathVariable int boardId, @ApiIgnore Authentication authentication, ReplyDto.replyCreate replyDto){
        log.info("댓글 등록");
        return replyService.createReply(boardId, authentication, replyDto);
    }

    /**
     * 댓글 삭제 API
     */
    @DeleteMapping("/recipe/{boardId}/reply/{replyId}")
    public void delete(@PathVariable int replyId) {
        log.info("댓글 삭제");
        replyService.deleteReply(replyId);
    }
}