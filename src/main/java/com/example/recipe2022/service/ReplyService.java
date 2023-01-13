package com.example.recipe2022.service;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.model.data.Reply;
import com.example.recipe2022.model.data.Users;
import com.example.recipe2022.model.dto.ReplyDto;
import com.example.recipe2022.model.repository.BoardRepository;
import com.example.recipe2022.model.repository.ReplyRepository;
import com.example.recipe2022.model.repository.UserRepository;
import com.example.recipe2022.model.vo.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final Response response;
    private final UserRepository userRepository;

    //생성
    public ResponseEntity<?> createReply(int boardSeq, Authentication authentication, ReplyDto.replyCreate replyDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();

        Board board = boardRepository.findById(boardSeq).orElseThrow(() -> new IllegalArgumentException("해당 boardId가 없습니다. id=" + boardSeq));
        Reply reply = Reply.builder()
                .build();
        replyRepository.save(reply);
        return response.success("댓글이 등록되었습니다.");
    }

    //삭제
    public ResponseEntity<?> deleteReply(int replySeq) {
        if(!replyRepository.existsById(replySeq)){
            return response.fail("댓글을 찾을 수 없습니다. ", HttpStatus.BAD_REQUEST);
        }
        replyRepository.deleteById(replySeq);
        return response.success("성공적으로 삭제되었습니다.");
    }




}
