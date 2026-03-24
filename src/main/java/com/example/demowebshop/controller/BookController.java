package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Book;
import com.example.demowebshop.service.intefaces.BookService;
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
    public String list(Model model) {
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
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getById(id));
        return "book/book-form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

}
