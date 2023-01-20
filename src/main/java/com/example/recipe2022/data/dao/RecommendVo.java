package com.example.recipe2022.data.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecommendVo {
    String recipeName;
    int weight;
}