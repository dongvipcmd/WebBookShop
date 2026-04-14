package com.example.demowebshop.service;

import com.example.demowebshop.entity.Book;
import com.example.demowebshop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    public Book getById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));
    }

    public void create(Book book){
        bookRepository.save(book);
    }

    public void update(Long id, Book newData){
        Book book = getById(id);

        book.setName(newData.getName());
        book.setPrice(newData.getPrice());
        book.setCategoryId(newData.getCategoryId());
        book.setAuthorId(newData.getAuthorId());
        book.setDescription(newData.getDescription());
        book.setStockQuantity(newData.getStockQuantity());

        if (newData.getImage() != null && !newData.getImage().isEmpty()) {
            book.setImage(newData.getImage());
        }

        bookRepository.save(book);
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }

    public void updateQuantity(Long id, Integer quantity) {
        Book book = getById(id);

        int current = book.getStockQuantity() == null ? 0 : book.getStockQuantity();
        book.setStockQuantity(current + quantity);

        bookRepository.save(book);
    }

    public void updateImage(Long id, String imagePath) {
        Book book = getById(id);
        book.setImage(imagePath);
        bookRepository.save(book);
    }
}