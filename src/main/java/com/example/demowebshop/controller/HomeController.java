package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Book;
import com.example.demowebshop.entity.Category;
import com.example.demowebshop.service.BookService;
import com.example.demowebshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String home(Model model) {
        // 1. Sách mới nhất
        model.addAttribute("newBooks", bookService.getTop5Newest());

        // 2. Sách bán chạy
        model.addAttribute("bestSellerBooks", bookService.getTop5BestSellers());

        // 3. Top 3 thể loại có nhiều sách nhất
        List<Category> top3Categories = categoryService.getTop3Categories();
        Map<Category, List<Book>> booksByTopCategories = new LinkedHashMap<>();

        for (Category category : top3Categories) {
            List<Book> booksInCat = bookService.getTop5ByCategory(category.getId());
            booksByTopCategories.put(category, booksInCat);
        }
        model.addAttribute("booksByTopCategories", booksByTopCategories);

        return "home";
    }
}