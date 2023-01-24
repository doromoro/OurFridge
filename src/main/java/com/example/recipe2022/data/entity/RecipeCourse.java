package com.example.recipe2022.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_recipe_course")
public class RecipeCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECIPE_COURSE_SEQ")
    private int recipeCourseSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_SEQ")
    private Recipe recipe;

    @Column(name = "RECIPE_ORDER")
    private int recipeOrder;

    @Lob
    private String contents;

    @Column(name = "file_grp_id")
    private String fileId;

    @Lob
    @Column(name = "tips")
    private String tips;

}