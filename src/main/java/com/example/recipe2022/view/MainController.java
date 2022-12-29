package com.example.recipe2022.view;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore

public class MainController {

    @GetMapping("/")
    public String hello() {
        return "hello";
    }
}
