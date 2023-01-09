package com.example.recipe2022.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecipeController {

    @GetMapping({"/recipe/main", "/recipe"})
    public String recipeMain(){
        return "recipe/recipe-main";
    }

    @GetMapping("/recipe/season")
    public String recipeSeason(){
        return "recipe/recipe-season";
    }

    @GetMapping("/recipe/ranking")
    public String recipeRanking(){
        return "recipe/recipe-ranking";
    }

    @GetMapping("/recipe/favorite")
    public String recipeFavorite(){
        return "recipe/recipe-favorite";
    }

    @GetMapping("/recipe/write")
    public String recipeWrite() {
        return "recipe/recipe-write";
    }
}
