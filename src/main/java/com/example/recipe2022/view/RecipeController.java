package com.example.recipe2022.view;

import com.example.recipe2022.model.data.Board;
import com.example.recipe2022.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

@Controller
public class RecipeController {

    @Autowired
    private BoardService boardService;

    @GetMapping({"/recipe/main", "/recipe"})
    public String index(Model model,
                        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false, defaultValue = "") String search) {
        Page<Board> boards = boardService.findByTitleContainingOrContentContaining(search, search, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "recipe/recipe-main";
    }

    @GetMapping("/recipe/{id}")
    public String findById(@PathVariable int id, Model model){
        model.addAttribute("board", boardService.detail(id));
        boardService.updateCount(id);
        return "recipe/recipe-detail";
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
