package com.example.recipe2022.model.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_refrigerator")
public class Fridge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refrigerator_seq")
    private int fridgeId;

    @Column(name = "refrigerator_nm")
    private String fridgeName;

    @Column(name = "refrigerator_detail")
    private String fridgeDetail;

    @Column(name = "refrigerator_favorite")
    private boolean fridgeFavorite;

    @ManyToOne
    @JoinColumn(name = "USER_SEQ")
    private Users user;
}
