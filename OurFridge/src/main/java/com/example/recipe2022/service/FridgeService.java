package com.example.recipe2022.service;

import com.example.recipe2022.data.dao.MyPageVo;
import com.example.recipe2022.data.dao.Response;
import com.example.recipe2022.data.dto.FridgeDto;
import com.example.recipe2022.data.entity.Fridge;
import com.example.recipe2022.data.entity.FridgeIngredient;
import com.example.recipe2022.data.entity.Ingredient;
import com.example.recipe2022.data.entity.Users;
import com.example.recipe2022.repository.FridgeIngredientRepository;
import com.example.recipe2022.repository.FridgeRepository;
import com.example.recipe2022.repository.IngredientRepository;
import com.example.recipe2022.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class FridgeService {
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final IngredientRepository ingredientRepository;

    private final UserRepository userRepository;
    private final Response response;
    private final FridgeRepository fridgeRepository;

    public ResponseEntity<?> createFridge(Authentication authentication, FridgeDto.newFridge fridgeDto) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        if (userRepository.findByEmail(email).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Users users = userRepository.findByEmail(email).orElseThrow();
        long currentCount = fridgeRepository.countByUser(users);
        if (currentCount > 4) { return response.fail("냉장고는 총 5개까지 만들수 있어요.", HttpStatus.BAD_REQUEST); }
        Fridge fridge = Fridge.builder()
                .fridgeDetail(fridgeDto.getFridgeDetail())
                .fridgeFavorite(false)
                .fridgeName(fridgeDto.getFridgeName())
                .user(users)
                .build();
        fridgeRepository.save(fridge);
        return response.success( ( "[ " + currentCount + " ] 번째 냉장고가 생성되었습니다") );
        // 로그인 유저를 기반으로 냉장고 객체를 가져옴.
    }
    public ResponseEntity<?> updateFridge(FridgeDto.updateFridge updateDto) {
        if (!fridgeRepository.existsByFridgeId(updateDto.getFridgeSeq())) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Fridge currentFridge = fridgeRepository.findByFridgeId(updateDto.getFridgeSeq()).orElseThrow();
        String updateName=updateDto.getFridgeName();
        String updateDetail=updateDto.getFridgeDetail();
        currentFridge.setFridgeName(updateName);
        currentFridge.setFridgeDetail(updateDetail);
        fridgeRepository.save(currentFridge);
        return response.success( "냉장고 수정 성공" );
    }
    public ResponseEntity<?> defaultFridge(FridgeDto.defaultFridge defaultDto) {
        if (!fridgeRepository.existsByFridgeId(defaultDto.getFridgeSeq())) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Fridge currentFridge = fridgeRepository.findByFridgeId(defaultDto.getFridgeSeq()).orElseThrow();
        currentFridge.setFridgeFavorite(!currentFridge.isFridgeFavorite());
        log.info("디폴트 냉장고 수정 -> " + currentFridge.isFridgeFavorite());
        fridgeRepository.save(currentFridge);
        return response.success("성공적으로 변경되었습니다.");
    }
    public ResponseEntity<?> deleteFridge(FridgeDto.deleteFridge deleteFridge) {
        if (!fridgeRepository.existsByFridgeId(deleteFridge.getFridgeSeq())) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        fridgeRepository.deleteByFridgeId(deleteFridge.getFridgeSeq());
        return response.success("성공적으로 삭제되었습니다.");
    }

    public ResponseEntity<?> putIngredientToFridge(FridgeDto.putIngredient putDto) {
        if (!ingredientRepository.existsByIngredientId(putDto.getIngredientId())) { return response.fail("검색 결과가 없습니다.", HttpStatus.BAD_REQUEST);}
        if (!fridgeRepository.existsByFridgeId(putDto.getFridgeSeq())) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Ingredient ingredient = ingredientRepository.findByIngredientId(putDto.getIngredientId()).orElseThrow();
        Fridge fridge = fridgeRepository.findByFridgeId(putDto.getFridgeSeq()).orElseThrow();
        FridgeIngredient fridgeIngredient = FridgeIngredient.builder()
                .ingredient(ingredient)
                .fridge(fridge)
                .build();
        if (!fridgeIngredientRepository.existsByIngredient(ingredient)) {
            fridgeIngredientRepository.save(fridgeIngredient);
        } else return response.fail("이미 냉장고에 있는 재료입니다.", HttpStatus.BAD_REQUEST);
        return response.success("n번 냉장고 특정 재료 추가");
    }
    public ResponseEntity<?> deleteIngredientToFridge(FridgeDto.deleteIngredient search) {
        if (!ingredientRepository.existsByIngredientId(search.getIngredientId())) {
            return response.fail("없는 재료에요", HttpStatus.BAD_REQUEST);
        }
        if (!fridgeRepository.existsByFridgeId(search.getFridgeSeq())) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Fridge a = fridgeRepository.findByFridgeId(search.getFridgeSeq()).orElseThrow();
        Ingredient b = ingredientRepository.findByIngredientId(search.getIngredientId()).orElseThrow();
        log.info("현재 삭제할려는 재료는 " +a.getFridgeId()+"번 냉장고에서"+ b.getIngredientName() + "입니다");
        int seq = fridgeIngredientRepository.findByFridgeAndIngredient(a, b).get().getFridgeDetailSeq();
        fridgeIngredientRepository.deleteByFridgeDetailSeq(seq);
        return response.success(search.getFridgeSeq()+"번 냉장고 특정 재료 삭제");
    }

    public ResponseEntity<?> viewMyFridgeIngredient(FridgeDto.viewIngredient search) {
        if (!fridgeRepository.existsByFridgeId(search.getFridgeSeq())) { return response.fail("냉장고를 찾을 수가 없습니다.", HttpStatus.BAD_REQUEST); }
        Fridge a = fridgeRepository.findByFridgeId(search.getFridgeSeq()).orElseThrow();
        List<FridgeIngredient> myIngredient = fridgeIngredientRepository.findAllByFridge(a);
        log.info("현재 선택된 냉장고는 " +a.getFridgeId()+"번 냉장고입니다. 냉장고 안 재료의 총 개수는 "+ myIngredient.size()+"개 입니다.");
        List<MyPageVo.myFridgeIngredientDetail> data =new ArrayList<>();
        for (FridgeIngredient fridgeIngredient : myIngredient) {
            MyPageVo.myFridgeIngredientDetail detailList = MyPageVo.myFridgeIngredientDetail.builder()
                    .ingredientName(fridgeIngredient.getIngredient().getIngredientName())
                    .ingredientType(fridgeIngredient.getIngredient().getIngredientType())
                    .build();
            data.add(detailList);
        }
        return response.success(data,search.getFridgeSeq() + "번 냉장고 재료 조회", HttpStatus.OK);
    }
}