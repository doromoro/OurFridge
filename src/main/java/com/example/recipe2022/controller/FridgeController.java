package com.example.recipe2022.controller;

import com.example.recipe2022.data.dto.FridgeDto;
import com.example.recipe2022.service.FridgeService;
import com.example.recipe2022.service.RecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "냉장고 관련 기능")
public class FridgeController {
    private final FridgeService fridgeService;
    private final RecommendService recommendService;
    @PostMapping(value = "/fridge-create")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 등록")
    public ResponseEntity<?> createFridge(@ApiIgnore Authentication authentication, @RequestBody FridgeDto.newFridge fridgeDto) {
        log.info("냉장고 등록");
        return fridgeService.createFridge(authentication, fridgeDto);}

    @PostMapping(value = "/fridge-update")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 수정")
    public ResponseEntity<?> updateFridge(
            @ApiIgnore Authentication authentication,
            @ApiParam(value = "냉장고 고유 번호")
            @RequestBody
            FridgeDto.updateFridge updateFridge){
        log.info("냉장고 수정");
        return fridgeService.updateFridge(authentication, updateFridge);
    }

    @PostMapping(value = "/fridge-default")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 즐겨찾기")
    public ResponseEntity<?> defaultFridge(
            @ApiIgnore Authentication authentication,
            @RequestBody
            @ApiParam(value = "냉장고 고유 번호")
            FridgeDto.defaultFridge defaultDto) {
        log.info("냉장고 즐겨찾기");
        return fridgeService.defaultFridge(authentication, defaultDto);
    }
    @PostMapping(value = "/fridge-delete")       //회원 가입 버튼
    @ApiOperation(value = "냉장고 삭제")
    public ResponseEntity<?> deleteFridge(
            @ApiIgnore Authentication authentication,
            @RequestBody
            @ApiParam(value = "냉장고 고유 번호")
            FridgeDto.deleteFridge deleteFridge) {
        log.info("냉장고 삭제");
        return fridgeService.deleteFridge(authentication, deleteFridge);
    }
    @PostMapping(value = "/fridge/put-ingredient")
    @ApiOperation(value = "냉장고에 재료 추가")
    public ResponseEntity<?> putIngredientToFridge(
            @ApiIgnore Authentication authentication,
            @RequestBody
            FridgeDto.putIngredient putDto) {
        log.info("냉장고에 재료 추가");
        return fridgeService.putIngredientToFridge(authentication, putDto);
    }

    @GetMapping(value = "/fridge/recommend")
    public ResponseEntity<?> recommend(
            @ApiIgnore Authentication authentication,
            @RequestBody
            FridgeDto.searchFridge fridgeSeq) {
        log.info("test");
        return recommendService.recommendRecipe(authentication, fridgeSeq);
    }
    @PostMapping(value = "/fridge/delete-ingredient")
    @ApiOperation(value = "n번 냉장고에서 재료 삭제")
    public ResponseEntity<?> deleteIngredientToFridge(
            @ApiIgnore Authentication authentication,
            @RequestBody
            FridgeDto.deleteIngredient search
    )
    {
        log.info("냉장고에 재료 추가");
        return fridgeService.deleteIngredientToFridge(authentication, search);
    }
    @GetMapping(value = "/fridge/view-ingredient")
    @ApiOperation(value = "n번 냉장고에서 재료 조회")
    public ResponseEntity<?> deleteIngredientToFridge(
            @ApiIgnore Authentication authentication,
            @RequestBody
            @ApiParam(value = "료가 있는 냉장고")
            FridgeDto.viewIngredient search
    )
    {
        log.info("냉장고에 재료 추가");
        return fridgeService.viewMyFridgeIngredient(authentication, search);
    }
}