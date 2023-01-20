package com.example.recipe2022.data.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RecommendDto {
    String recipeName;
    int weight;
    List<?> insufficientList = new ArrayList<>();
}