package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Author;
import com.example.demowebshop.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public String list(Model model){
        model.addAttribute("authors", authorService.getAll());
        return "author/author-list";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("author", new Author());
        return "author/author-form";
    }

    @PostMapping("")
    public String create(@ModelAttribute Author author){
        authorService.createAuthor(author);
        return "redirect:/authors";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("author", authorService.getById(id));
        return "author/author-form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Author author){
        authorService.updateAuthor(id, author);
        return "redirect:/authors";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }

}
