package com.example.recipe2022.data.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "recipe_seq")
    private int recipeSeq;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "file_grp_id")
    private String file;

    @Column(name = "food_class_kor_nm")
    private String foodClassName;

    @Column(name = "food_class_type_code")
    private String foodClassTypeCode;

    @Column(name = "volume")
    private String volume;

    @Column(name = "recipe_time")
    private String time;

    @Column(name = "recipe_level")
    private int level;


    @Column
    private boolean recipeFavorite;

    @OneToMany(mappedBy = "recipe")
    List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    List<RecipeCourse> recipeCourses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "USER_SEQ")
    @ToString.Exclude
    private Users user;

    @ColumnDefault("0")
    @Column(name = "view_cnt")
    private int viewCnt;

    @Column(name = "favorite")
    private int favorite;
    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<FavoriteRecipe> favoriteRecipes;
    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private List<Reply> reply;





}