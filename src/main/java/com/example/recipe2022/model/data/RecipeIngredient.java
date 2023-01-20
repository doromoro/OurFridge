package com.example.recipe2022.model.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_recipe_ingredient")
public class RecipeIngredient extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_INGREDIENT_SEQ")
    private int recipeIngredientSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_SEQ")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INGREDIENT_SEQ")
    private Ingredient ingredient;

    @Column(name = "VOLUME")
    private String volume;
}
