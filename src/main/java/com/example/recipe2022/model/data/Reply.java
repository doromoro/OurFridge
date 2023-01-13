package com.example.recipe2022.model.data;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reply")
public class Reply extends BaseTimeEntity{
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
    @Lob
    private String contents;
    @Column
    private String recommend_cnt;
    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Users user;

}
