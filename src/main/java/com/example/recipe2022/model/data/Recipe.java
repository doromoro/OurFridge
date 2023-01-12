package com.example.recipe2022.model.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_seq")
    private int recipeId;

    @Column(name = "title")
    private String recipeTitle;

    @Column(name = "contents")
    private String recipeContents;

    @Column(name = "file_grp_id")
    private String recipeFile;

    @Column(name = "food_class_code")
    private int recipeFoodCode;

    @Column(name = "volume")
    private String recipeVolume;

    @Column(name = "recipe_time")
    private String recipeTime;

    @Column(name = "recipe_level")
    private int recipeLevel;

    @ManyToOne
    @JoinColumn(name = "USER_SEQ")
    @ToString.Exclude
    private Users user;

}
