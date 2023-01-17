package com.example.recipe2022.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_ingredient")
public class Ingredient extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_seq")
    private int ingredientId;

    @Column(name = "ingredient_kor_nm")
    private String ingredientName;

    @Column(name = "ingredient_type")
    private String ingredientType;

    @Column(name = "ingredient_type_code")
    private String ingredientTypeCode;

    @OneToMany(mappedBy = "ingredient")
    private List<FridgeIngredient> fridgeIngredients = new ArrayList<>();
}