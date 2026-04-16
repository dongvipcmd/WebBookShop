package com.example.demowebshop.controller;

import com.example.demowebshop.service.AuthorService;
import com.example.demowebshop.service.BookService;
import com.example.demowebshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.math.BigDecimal;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final BookService bookService;
    private final CategoryService categoryService; // Thêm service thể loại
    private final AuthorService authorService;     // Thêm service tác giả

    @GetMapping
    public String shop(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "newest") String sortBy,
            Model model){

        // Lấy danh sách sách đã lọc
        model.addAttribute("books", bookService.filterBooks(categoryId, authorId, maxPrice, keyword, sortBy));

        // Truy vấn dữ liệu cho các bộ lọc
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("authors", authorService.getAll());

        // Giữ lại trạng thái người dùng đã chọn trên giao diện
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("currentAuthorId", authorId);
        model.addAttribute("currentMaxPrice", maxPrice);
        model.addAttribute("currentSortBy", sortBy);
        model.addAttribute("currentKeyword", keyword);

        return "shop/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        model.addAttribute("book", bookService.getById(id));
        return "shop/detail";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public List<Map<String, Object>> searchLive(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        return bookService.searchLive(keyword).stream().map(b -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", b.getId());
            map.put("name", b.getName());
            map.put("image", b.getImage());
            map.put("price", b.getPrice());
            return map;
        }).collect(Collectors.toList());
    }
}