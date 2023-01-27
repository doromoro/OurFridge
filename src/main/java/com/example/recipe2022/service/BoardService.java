package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.BoardDto;
import com.example.recipe2022.data.entity.Board;
import com.example.recipe2022.data.entity.FavoriteBoard;
import com.example.recipe2022.data.entity.Users;
import com.example.recipe2022.repository.BoardRepository;
import com.example.recipe2022.repository.FavoriteBoardRepository;
import com.example.recipe2022.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final Response response;
    private final FavoriteBoardRepository favoriteBoardRepository;

    /**
     * 검색(제목, useYN)
     */
    @Transactional
    public Page<Board> findByUseYNAndTitleContaining(Character useYN, String title, Pageable pageable) {
        return boardRepository.findByUseYNAndTitleContaining(useYN, title, pageable);
    }

    @Transactional
    public Page<FavoriteBoard> findByUseYNAndUser(Character useYN, Authentication authentication, Pageable pageable) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        return favoriteBoardRepository.findByUseYNAndUser(useYN, users, pageable);
    }

    @Transactional
    public ResponseEntity<?> searchBoards(Pageable pageable, String search) {


        Page<Board> boards = boardRepository.findByUseYNAndTitleContaining('Y', search, pageable);
        List<BoardDto.boardSimpleDto> data = new ArrayList<>();

        for(Board board : boards){
            Users user = board.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getUserSeq()).get().getName();
            BoardDto.boardSimpleDto boardLists = BoardDto.boardSimpleDto.builder()
                    .file(board.getFileId())
                    .title(board.getTitle())
                    .count(board.getViewCnt())
                    .name(userName)
                    .build();
            data.add(boardLists);
        }
        return response.success(data, "전체 레시피가 조회되었습니다!", HttpStatus.OK);
    }
    /**
     * 즐겨찾기한 레시피
     */
    @Transactional
    public ResponseEntity<?> favoritedBoards(Pageable pageable, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();
        List<FavoriteBoard> boards = favoriteBoardRepository.findAllByUseYNAndUser('Y', users);
        List<BoardDto.boardSimpleDto> data = new ArrayList<>();
        for(FavoriteBoard favoritedBoard : boards){
            Users user = favoritedBoard.getUser();
            String userEmail = user.getEmail();
            Users writeUser = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(writeUser.getUserSeq()).get().getName();
            BoardDto.boardSimpleDto boardLists = BoardDto.boardSimpleDto.builder()
                    .file(favoritedBoard.getBoard().getFileId())
                    .title(favoritedBoard.getBoard().getTitle())
                    .count(favoritedBoard.getBoard().getViewCnt())
                    .name(userName)
                    .build();
            data.add(boardLists);
        }
        return response.success(data, "즐겨찾기한 레시피가 조회되었습니다!", HttpStatus.OK);
    }

    /**
     * 즐겨찾기하기
     */
    @Transactional
    public ResponseEntity favoritedRegisterRecipe(Authentication authentication, int boardSeq) {
        if(!boardRepository.existsByBoardSeq(boardSeq)){
            return response.fail("레시피를 찾을 수 없습니다!", HttpStatus.BAD_REQUEST);
        }
        Board board = boardRepository.findByBoardSeq(boardSeq).orElseThrow();
        if(board.getUseYN() == 'N'){
            return response.fail("해당 게시물이 삭제되었습니다.",HttpStatus.BAD_REQUEST);
        }
        Board currentBoard = boardRepository.findByBoardSeq(boardSeq).orElseThrow();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Users users = userRepository.findByEmail(email).orElseThrow();

        if(favoriteBoardRepository.findByBoardAndUser(currentBoard, users) == null) {
            // 즐겨찾기를 누른적 없다면 Favorite 생성 후, 즐겨찾기 처리
            currentBoard.setFavorite(currentBoard.getFavorite() + 1);
            FavoriteBoard favorite = new FavoriteBoard(currentBoard, users); // true 처리
            favoriteBoardRepository.save(favorite);
            return response.success("즐겨찾기 처리 완료");
        } else {
            // 즐겨찾기 누른적 있다면 즐겨찾기 처리 후 테이블 삭제
            FavoriteBoard favorite = favoriteBoardRepository.findFavoriteByBoard(currentBoard);
            currentBoard.setFavorite(currentBoard.getFavorite() - 1);
            favoriteBoardRepository.delete(favorite);
            return response.success("즐겨찾기 취소 완료");
        }
    }

    /**
     * 글 조회수 로직
     */
    @Transactional
    public int updateCount(int id) {
        return boardRepository.updateCount(id);
    }

    /**
     * 글목록 로직
     */
    @Transactional
    public Page<Board> findByUseYN(Character useYN, Pageable pageable) {
        return boardRepository.findByUseYN(useYN, pageable);
    }
    @Transactional
    public ResponseEntity<?> mainBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findByUseYN('Y', pageable);
        List<BoardDto.boardSimpleDto> data = new ArrayList<>();

        for(Board board : boards){
            Users user = board.getUser();
            String userEmail = user.getEmail();
            Users users = userRepository.findByEmail(userEmail).orElseThrow();
            String userName = userRepository.findById(users.getUserSeq()).get().getName();
            BoardDto.boardSimpleDto boardLists = BoardDto.boardSimpleDto.builder()
                    .file(board.getFileId())
                    .title(board.getTitle())
                    .count(board.getViewCnt())
                    .name(userName)
                    .contents(board.getContents())
                    .build();
            data.add(boardLists);
        }
        return response.success(data, "전체 레시피가 조회되었습니다!", HttpStatus.OK);
    }

}