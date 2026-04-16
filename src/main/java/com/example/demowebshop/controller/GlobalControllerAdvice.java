package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Author;
import com.example.demowebshop.entity.Category;
import com.example.demowebshop.service.AuthorService;
import com.example.demowebshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoryService categoryService;
    private final AuthorService authorService;

    @ModelAttribute("categories")
    public List<Category> getCategories(){
        return categoryService.getAll();
    }

    @ModelAttribute("authors")
    public List<Author> authors(){
        return authorService.getAll();
    }
}