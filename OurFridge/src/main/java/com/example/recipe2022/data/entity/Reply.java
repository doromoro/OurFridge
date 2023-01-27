package com.example.recipe2022.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_comment")
public class Reply extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private int replySeq;
    @ManyToOne
    @JoinColumn(name = "recipe_seq")
    private recipe recipe;
    @Column(name = "parent_comment_seq")
    private int parentCommentSeq;

    @Column(name = "depth")
    private int depth;

    @Lob
    @NotNull
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Users user;

}