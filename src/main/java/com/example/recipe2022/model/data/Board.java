package com.example.recipe2022.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@Table(name = "board")
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
    private String content;
    @ManyToOne(fetch = FetchType.EAGER) // Many = board, User = One
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<Reply> reply;

//    @Column
//    private String file_grp_id;

    @Column(name = "recommend_cnt")
    private int recommend;

    @Column(name = "view_cnt")
    private int view;

    @Column
    private String use_yn;

    @ManyToOne(fetch = FetchType.EAGER) // Many = board, User = One
    @JoinColumn(name = "create_seq")
    private User create_seq;

    @CreationTimestamp
    private Timestamp create_date;

//    @Column
//    private String modify_seq;
//    @Column
//    private Timestamp modify_date;


}
