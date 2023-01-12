package com.example.recipe2022.service;

import com.example.recipe2022.model.data.Ingredient;
import com.example.recipe2022.model.repository.IngredientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("asdasd")
@ExtendWith(SpringExtension.class)
@DataJpaTest
class IngredientServiceTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void save() {
        // given
        String roleName = "ADMIN";
        final Ingredient ingredient = Ingredient.builder()
                .ingredientName(roleName).build();

        // when
        final Ingredient savedRole = ingredientRepository.save(ingredient);

        // then
        assertEquals(roleName, savedRole.getIngredientName());
    }
}