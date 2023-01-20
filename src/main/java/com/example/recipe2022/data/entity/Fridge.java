package com.example.recipe2022.data.entity;

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
@Table(name = "t_refrigerator")
public class Fridge {
    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refrigerator_seq")
    private int fridgeId;

    @Column(name = "refrigerator_nm")
    private String fridgeName;

    @Column(name = "refrigerator_detail")
    private String fridgeDetail;

    @Column(name = "refrigerator_default")
    private boolean fridgeFavorite;

    @ManyToOne
    @JoinColumn(name = "USER_SEQ")
    @ToString.Exclude
    private Users user;

    @OneToMany(mappedBy = "fridge")
    List<FridgeIngredient> fridgeIngredients = new ArrayList<>();
}