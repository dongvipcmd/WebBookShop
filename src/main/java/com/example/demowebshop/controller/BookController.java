package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Book;
import com.example.demowebshop.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String list(Model model){
        model.addAttribute("books", bookService.getAll());
        return "book/book-list";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("book", new Book());
        return "book/book-form";
    }

    @PostMapping
    public String create(@ModelAttribute Book book){
        bookService.create(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("book", bookService.getById(id));
        return "book/book-form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Book book){
        bookService.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/update-quantity/{id}")
    public String showUpdateQuantityForm(@PathVariable Long id, Model model){
        model.addAttribute("book", bookService.getById(id));
        return "book/update-quantity";
    }

    @PostMapping("/update-quantity/{id}")
    public String updateQuantity(@PathVariable Long id,
                                 @RequestParam Integer quantity) {

        bookService.updateQuantity(id, quantity);
        return "redirect:/books";
    }


}

