package com.example.recipe2022.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping(value = "/admin/members/new")
    public String itemForm() {
        return "member/adminFrom";
    }
}
