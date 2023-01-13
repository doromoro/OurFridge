package com.example.recipe2022.view;

import com.example.recipe2022.model.dto.FridgeDto;
import com.example.recipe2022.service.FridgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "냉장고 관련 기능")
public class FridgeController {
    private final FridgeService fridgeService;

    @PostMapping(value = "/fridge-create")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 등록")
    public ResponseEntity<?> createFridge(Authentication authentication, FridgeDto.fridgeCreate fridgeDto) {
        log.info("냉장고 등록");
        return fridgeService.createFridge(authentication, fridgeDto);}
    @PostMapping(value = "/fridge-delete")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 삭제")
    public ResponseEntity<?> deleteFridge(
            @RequestParam
            @ApiParam(value = "냉장고 고유 번호")
            int fridgeSeq) {
        log.info("냉장고 삭제");
        return fridgeService.deleteFridge(fridgeSeq);
    }

    @PostMapping(value = "/fridge-default")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 즐겨찾기")
    public ResponseEntity<?> defaultFridge(
            @RequestParam
            @ApiParam(value = "냉장고 고유 번호")
            int fridgeSeq) {
        log.info("냉장고 즐겨찾기");
        return fridgeService.defaultFridge(fridgeSeq);
    }

    @PostMapping(value = "/fridge-update")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 수정")
    public ResponseEntity<?> updateFridge(
            @RequestParam
            @ApiParam(value = "냉장고 고유 번호")
            int fridgeSeq, FridgeDto.fridgeCreate fridgeDto) {
        log.info("냉장고 수정");
        return fridgeService.updateFridge(fridgeSeq, fridgeDto);
    }

    @PostMapping(value = "/fridge/put-ingredient")
    @ApiOperation(value = "냉장고에 재료 추가")
    public ResponseEntity<?> putIngredientToFridge(
            @ApiParam(value = "재료 이름")
            String name,
            @ApiParam(value = "냉장고 고유 번호")
            int fridgeSeq) {
        log.info("냉장고에 재료 추가");
        return fridgeService.putIngredientToFridge(name, fridgeSeq);
    }
}