package com.example.recipe2022.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_refrigerator_detail")
public class FridgeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIDGE_DETAIL_SEQ")
    private int fridgeDetailSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refrigerator_seq")
    private Fridge fridge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_seq")
    private Ingredient ingredient;

    @Column(name = "DISPLAY_SEQUENCE")
    private int displaySeq;

}