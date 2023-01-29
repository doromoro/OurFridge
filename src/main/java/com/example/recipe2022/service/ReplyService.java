package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.ReplyDto;
import com.example.recipe2022.data.entity.Recipe;
import com.example.recipe2022.data.entity.Reply;
import com.example.recipe2022.data.entity.Users;
import com.example.recipe2022.repository.RecipeRepository;
import com.example.recipe2022.repository.ReplyRepository;
import com.example.recipe2022.repository.UserRepository;
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
    private final RecipeRepository recipeRepository;
    private final Response response;
    private final UserRepository userRepository;

    //생성
    public ResponseEntity<?> createReply(Authentication authentication, ReplyDto.replyCreate replyDto){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();

        Recipe recipe = recipeRepository.findByRecipeSeq(replyDto.getRecipeSeq()).orElseThrow(() -> new IllegalArgumentException("해당 recipeId가 없습니다. id=" + replyDto.getRecipeSeq()));
        Reply reply = Reply.builder()
                .contents(replyDto.getReplyContents())
                .recipe(recipe)
                .user(users)
                .build();
        replyRepository.save(reply);
        return response.success("댓글이 등록되었습니다.");
    }

    //삭제
    public ResponseEntity<?> deleteReply(Authentication authentication, ReplyDto.replyDelete replyDeleteDto) {
        if(!replyRepository.existsById(replyDeleteDto.getReplySeq())){
            return response.fail("댓글을 찾을 수 없습니다. ", HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        if (userRepository.findByEmail(email).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Users users = userRepository.findByEmail(email).orElseThrow();
        Reply currentReply = replyRepository.findByReplySeq(replyDeleteDto.getReplySeq());
        if (!users.getReplies().contains(currentReply)) {
            return response.fail("해당 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        replyRepository.deleteById(replyDeleteDto.getReplySeq());
        return response.success("성공적으로 삭제되었습니다.");
    }




}