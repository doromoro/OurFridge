package com.example.recipe2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Recipe2022Application {

	public static void main(String[] args) {
		SpringApplication.run(Recipe2022Application.class, args);
	}

}
