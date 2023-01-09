package com.example.recipe2022.view;

import com.example.recipe2022.model.dto.FridgeDto;
import com.example.recipe2022.model.vo.Response;
import com.example.recipe2022.service.FridgeService;
import com.example.recipe2022.service.Helper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FridgeController {

    private final Response response;
    private final FridgeService fridgeService;

    @PostMapping(value = "/fridge-create")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 등록")
    public ResponseEntity<?> createFridge(@ApiIgnore Authentication authentication, FridgeDto.fridgeCreate fridgeDto) {
        log.info("냉장고 등록");
        return fridgeService.createFridge(authentication, fridgeDto);
    }
    @PostMapping(value = "/fridge-delete")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 삭제")
    public ResponseEntity<?> deleteFridge(@RequestParam int fridgeSeq) {
        log.info("냉장고 삭제");
        return fridgeService.deleteFridge(fridgeSeq);
    }

    @PostMapping(value = "/fridge-default")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 즐겨찾기")
    public ResponseEntity<?> defaultFridge(@RequestParam int fridgeSeq) {
        log.info("냉장고 즐겨찾기");
        return fridgeService.defaultFridge(fridgeSeq);
    }

    @PostMapping(value = "/fridge-update")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 수정")
    public ResponseEntity<?> updateFridge(@RequestParam int fridgeSeq, FridgeDto.fridgeCreate fridgeDto) {
        log.info("냉장고 수정");
        return fridgeService.updateFridge(fridgeSeq, fridgeDto);
    }


}
