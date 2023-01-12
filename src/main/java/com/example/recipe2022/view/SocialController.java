package com.example.recipe2022.view;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/")
@ApiIgnore

public class SocialController {
    @GetMapping("/social")
    public String socialSuccess(Model model,
                                @RequestParam(value = "provider", required = false) String provider,
                                @RequestParam(value = "oauthId", required = false) String oauthId
    ) {
        model.addAttribute("provider", provider);
        model.addAttribute("oauthId", oauthId);
        return "hello";
    }
}