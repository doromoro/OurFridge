package com.example.recipe2022.controller;

import com.example.recipe2022.data.dto.ReplyDto;
import com.example.recipe2022.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping("/recipe/reply-create")
    @ApiOperation(value = "댓글 등록")
    public ResponseEntity<?> createReply(@ApiIgnore Authentication authentication,
                                         @RequestBody ReplyDto.replyCreate replyDto){
        log.info("댓글 등록");
        return replyService.createReply(authentication, replyDto);
    }

    /**
     * 댓글 삭제 API
     */
    @DeleteMapping("/recipe/reply-delete")
    @ApiOperation(value = "댓글 삭제")
    public ResponseEntity<?> deleteReply(@ApiIgnore Authentication authentication,
                                         @RequestBody ReplyDto.replyDelete replyDeleteDto) {
            log.info("댓글 삭제");
        return replyService.deleteReply(authentication, replyDeleteDto);
    }
}