package com.example.recipe2022.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private int id;
    @ManyToOne
    @JoinColumn(name = "board_seq")
    private Board board;
    @Column
    private int parent_comment_seq;
    @Column
    private int depth;
    @Column
    private String contents;
    @Lob
    private String recommend_cnt;
    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

//    @ColumnDefault("Y")
    @Column
    private String use_yn;
    @Column
    private String create_seq;
    @CreationTimestamp
    private Timestamp create_date;
    @Column
    private String modify_seq;

    @Column
    private Timestamp modify_date;
}
