package com.example.recipe2022.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/member")
@ApiIgnore

public class SocialController {
    @GetMapping("/social")
    public String socialSuccess(Model model,
                                @RequestParam(value = "provider", required = false) String provider,
                                @RequestParam(value = "oauthId", required = false) String oauthId
    ) {
        model.addAttribute("provider", provider);
        model.addAttribute("oauthId", oauthId);
        return "social-success";
    }
}