package com.example.demowebshop.service.intefaces;

import com.example.demowebshop.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAll();
    Book getById(Long id);
    Book saveBook(Book book);
    void deleteBook(Long id);


}
