package com.example.recipe2022.data.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_code")
public class Codes extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_seq")
    private int codeSeq;

    @Column(name = "grp_code_id")
    private String grpCodeId;

    @Column(name = "code_id")
    private String codeId;

    @Column(name = "code_nm")
    private String codeNm;

    @Lob
    private String rmk;
}