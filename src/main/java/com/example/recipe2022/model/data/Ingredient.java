package com.example.recipe2022.model.data;

import lombok.*;

import javax.persistence.*;

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

    @Column(name = "ingredient_volume")
    private String ingredientVolume;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "ingredient_type")
    private String ingredientType;

    @Column(name = "ingredient_type_code")
    private String ingredientTypeCode;

    @Column(name = "ingredient_order")
    private int ingredientOrder;
}
