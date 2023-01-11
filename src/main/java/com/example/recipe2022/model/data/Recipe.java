package com.example.recipe2022.model.data;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_seq")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "file_grp_id")
    private String file;

    @Column(name = "food_class_code")
    private int foodCode;

    @Column(name = "volume")
    private String volume;

    @Column(name = "recipe_time")
    private String time;

    @Column(name = "recipe_level")
    private int level;

}
