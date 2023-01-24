package com.example.recipe2022.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_comment")
public class Reply{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private int id;

    @ManyToOne
    @JoinColumn(name = "board_seq")
    private Board board;

    @Column(name = "parent_comment_seq")
    private int parentCommentSeq;

    @Column(name = "depth")
    private int depth;

    @Lob
    @Column(name = "Contents")
    private String contents;

    @Column(name = "recommend_cnt")
    private String recommendCnt;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Users user;

}