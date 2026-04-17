package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Book;
import com.example.demowebshop.service.AuthorService;
import com.example.demowebshop.service.BookService;
import com.example.demowebshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final AuthorService authorService;

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
    public String create(@ModelAttribute Book book,
                         @RequestParam(value = "imageFile", required = false) MultipartFile file) {

        try {
            if (file != null && !file.isEmpty()) {

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path path = Paths.get("uploads/" + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                book.setImage("/uploads/" + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        bookService.create(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("book", bookService.getById(id));
        return "book/book-form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Book book,
                         @RequestParam(value = "imageFile", required = false) MultipartFile file){

        try {
            Book oldBook = bookService.getById(id);

            if (file != null && !file.isEmpty()) {
                if (oldBook.getImage() != null) {
                    String oldImagePath = oldBook.getImage().replace("/uploads/", "uploads/");
                    Path oldPath = Paths.get(oldImagePath);
                    Files.deleteIfExists(oldPath);
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path path = Paths.get("uploads/" + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                book.setImage("/uploads/" + fileName);
            } else {
                book.setImage(oldBook.getImage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
