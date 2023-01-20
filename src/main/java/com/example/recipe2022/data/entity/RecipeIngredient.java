package com.example.recipe2022.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_recipe_ingredient")
public class RecipeIngredient{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_INGREDIENT_SEQ")
    private int recipeIngredientSeq;

    @ManyToOne
    @JoinColumn(name = "RECIPE_SEQ")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "INGREDIENT_SEQ")
    private Ingredient ingredient;

    @Column(name = "VOLUME")
    private String volume;
}