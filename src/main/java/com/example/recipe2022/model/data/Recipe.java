package com.example.recipe2022.model.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_recipe")
public class Recipe extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_SEQ")
    private int recipeId;

    @Column(name = "RECIPE_NM")
    private String recipeName;

    @Column(name = "contents")
    private String recipeContents;

    @Column(name = "type_code")
    private int recipeTypeCode;

    @Column(name = "type")
    private String recipeType;

    @Column(name = "food_class_code")
    private int foodClassCode;

    @Column(name = "food_class")
    private String foodClass;

    @Column(name = "recipe_time")
    private String recipeTime;

    @Column(name = "calorie")
    private String calorie;

    @Column(name = "serving")
    private String serving;

    @Column(name = "recipe_level")
    private int recipeLevel;

    @Column(name = "represent_img")
    private String representImg;

    @Column(name = "detail_url")
    private String detailUrl;
}
