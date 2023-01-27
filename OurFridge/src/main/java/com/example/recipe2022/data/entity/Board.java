package com.example.recipe2022.data.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_recipe")
public class recipe extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_seq")
    private int recipeSeq;

    @Column(name = "recipe_div_cd", nullable=false)
    private String recipeDivCd;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.EAGER) // Many = recipe, User = One
    @JoinColumn(name = "user_seq")
    // @ToString.Exclude
    private Users user;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private List<Reply> reply;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    private List<Favoriterecipe> favoriterecipes;

    @Column(name = "FILE_GRP_ID")
    private String fileId;

    @ColumnDefault("0")
    @Column(name = "view_cnt")
    private int viewCnt;

    @Column(name = "favorite")
    private int favorite;


}