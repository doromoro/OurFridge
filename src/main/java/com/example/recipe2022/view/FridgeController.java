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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "냉장고 관련 기능")
@CrossOrigin(origins = "https://localhost:3000")
public class FridgeController {
    private final FridgeService fridgeService;

    @PostMapping(value = "/fridge-create")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 등록")
    public ResponseEntity<?> createFridge(@ApiIgnore Authentication authentication, @RequestBody FridgeDto.fridgeCreate fridgeDto) {
        log.info("냉장고 등록");
        return fridgeService.createFridge(authentication, fridgeDto);}


    @PostMapping(value = "/fridge-delete")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 삭제")
    public ResponseEntity<?> deleteFridge(
            @RequestBody
            @ApiParam(value = "냉장고 고유 번호")
            int fridgeSeq) {
        log.info("냉장고 삭제");
        return fridgeService.deleteFridge(fridgeSeq);
    }

    @PostMapping(value = "/fridge-default")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 즐겨찾기")
    public ResponseEntity<?> defaultFridge(
            @RequestBody
            @ApiParam(value = "냉장고 고유 번호")
            int fridgeSeq) {
        log.info("냉장고 즐겨찾기");
        return fridgeService.defaultFridge(fridgeSeq);
    }

    @PostMapping(value = "/fridge-update")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 수정")
    public ResponseEntity<?> updateFridge(
            @ApiParam(value = "냉장고 고유 번호")
            @RequestBody
            int fridgeSeq,
            @RequestBody
            FridgeDto.fridgeCreate fridgeDto) {
        log.info("냉장고 수정");
        return fridgeService.updateFridge(fridgeSeq, fridgeDto);
    }

    @PostMapping(value = "/fridge/put-ingredient")
    @ApiOperation(value = "냉장고에 재료 추가")
    public ResponseEntity<?> putIngredientToFridge(
            @ApiParam(value = "재료 이름")
            int seq,
            @ApiParam(value = "냉장고 고유 번호")
            @RequestBody
            int fridgeSeq) {
        log.info("냉장고에 재료 추가");
        return fridgeService.putIngredientToFridge(seq, fridgeSeq);
    }

    @PostMapping(value = "/fridge/delete-ingredient")
    @ApiOperation(value = "n번 냉장고에서 재료 삭제")
    public ResponseEntity<?> deleteIngredientToFridge(
            @RequestBody
            @ApiParam(value = "재료 고유 번호")
            int ingSeq,
            @RequestBody
            @ApiParam(value = "삭제할 재료가 있는 냉장고")
            int fridgeSeq
    )
    {
        log.info("냉장고에 재료 추가");
        return fridgeService.deleteIngredientToFridge(ingSeq, fridgeSeq);
    }
    @GetMapping(value = "/fridge/view-ingredient")
    @ApiOperation(value = "n번 냉장고에서 재료 조회")
    public ResponseEntity<?> deleteIngredientToFridge(
            @RequestBody
            @ApiParam(value = "료가 있는 냉장고")
            int fridgeSeq
    )
    {
        log.info("냉장고에 재료 추가");
        return fridgeService.viewMyFridgeIngredient(fridgeSeq);
    }
}