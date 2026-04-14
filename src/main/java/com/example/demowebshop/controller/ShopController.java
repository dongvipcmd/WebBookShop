package com.example.demowebshop.controller;

import com.example.demowebshop.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final BookService bookService;

    @GetMapping
    public String shop(Model model){
        model.addAttribute("books", bookService.getAll());
        return "shop/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        model.addAttribute("book", bookService.getById(id));
        return "shop/detail";
    }
}