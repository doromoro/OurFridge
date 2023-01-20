package com.example.recipe2022.model.data;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_board")
public class Board extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_seq")
    private int id;

    @Column(name = "board_div_cd", nullable=false)
    private String div;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.EAGER) // Many = board, User = One
    @JoinColumn(name = "user_seq")
    @ToString.Exclude
    private Users user;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Reply> reply;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<FavoriteBoard> favoriteBoards;

    @Column
    private String file_grp_id;

    @ColumnDefault("0")
    @Column(name = "recommend_cnt")
    private int recommend;

    @Column(name = "view_cnt")
    private int view;

    @Column(name = "favorited")
    private int favorited;




}
