package com.example.recipe2022.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RecommendDto {
    String recipeName;
    int weight;
    List<?> insufficientList;
}