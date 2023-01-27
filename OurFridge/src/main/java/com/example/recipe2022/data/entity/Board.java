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
@Table(name = "t_board")
public class Board extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_seq")
    private int boardSeq;

    @Column(name = "board_div_cd", nullable=false)
    private String boardDivCd;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    private String contents;
    @ManyToOne(fetch = FetchType.EAGER) // Many = board, User = One
    @JoinColumn(name = "user_seq")
    // @ToString.Exclude
    private Users user;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Reply> reply;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<FavoriteBoard> favoriteBoards;

    @Column(name = "FILE_GRP_ID")
    private String fileId;

    @ColumnDefault("0")
    @Column(name = "view_cnt")
    private int viewCnt;

    @Column(name = "favorite")
    private int favorite;


}