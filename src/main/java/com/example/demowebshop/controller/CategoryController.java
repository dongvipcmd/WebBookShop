package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Category;
import com.example.demowebshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model){
        model.addAttribute("categories", categoryService.getAll());
        return "category/category-list";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("category", new Category());
        return "category/category-form";
    }

    @PostMapping
    public String create(@ModelAttribute Category category){
        categoryService.create(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("category", categoryService.getById(id));
        return "category/category-form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Category category){
        categoryService.update(id, category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
