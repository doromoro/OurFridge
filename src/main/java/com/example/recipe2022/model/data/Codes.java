package com.example.recipe2022.model.data;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_code")
public class Codes extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_seq")
    private int grpCodeSeq;

    @Column(name = "grp_code_id")
    private String grpCodeId;

    @Column(name = "code_id")
    private String codeId;

    @Column(name = "code_nm")
    private String codeNm;

//    @Column(name = "disp_seq")
//    private int dispSeq;

    @Lob
    private String rmk;

//    @ManyToOne
//    @JoinColumn(name = "USER_SEQ")
//    @ToString.Exclude
//    private Users user;
//
//    @OneToMany(mappedBy = "fridge")
//    List<FridgeIngredient> fridgeIngredients = new ArrayList<>();
}
