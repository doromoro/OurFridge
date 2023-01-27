package com.example.recipe2022.data.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_favorite_recipe")
public class FavoriteRecipe extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_seq", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @Column(nullable = false)
    private boolean status; // true = 즐겨찾기, false = 즐겨찾기 취소

    public FavoriteRecipe(Recipe recipe, Users user) {
        this.recipe = recipe;
        this.user = user;
        this.status = true;
    }

}