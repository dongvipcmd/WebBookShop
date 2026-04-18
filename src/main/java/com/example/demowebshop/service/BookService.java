package com.example.demowebshop.service;

import com.example.demowebshop.entity.Book;
import com.example.demowebshop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final FileService fileService;

    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    public Book getById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));
    }

    public void create(Book book, MultipartFile file) {

        if (file != null && !file.isEmpty()) {
            String imagePath = fileService.saveFile(file);
            book.setImage(imagePath);
        }

        bookRepository.save(book);
    }

    public void update(Long id, Book newData, MultipartFile file){

        Book book = getById(id);

        book.setName(newData.getName());
        book.setPrice(newData.getPrice());
        book.setCategoryId(newData.getCategoryId());
        book.setAuthorId(newData.getAuthorId());
        book.setDescription(newData.getDescription());
        book.setStockQuantity(newData.getStockQuantity());

        if (file != null && !file.isEmpty()) {
            fileService.deleteFile(book.getImage());
            String imagePath = fileService.saveFile(file);
            book.setImage(imagePath);
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

    public List<Book> filterBooks(Long categoryId, Long authorId, BigDecimal maxPrice, String keyword, String sortBy) {
        // Mặc định là mới nhất (sắp xếp theo id giảm dần)
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        if ("priceAsc".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "price"); // Giá thấp nhất
        } else if ("priceDesc".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "price"); // Giá cao nhất
        } else if ("bestSelling".equals(sortBy)) {
            // Lưu ý: Cần thêm thuộc tính 'soldQuantity' vào entity Book để dùng được tính năng này
            // sort = Sort.by(Sort.Direction.DESC, "soldQuantity");
        }

        return bookRepository.filterBooks(categoryId, authorId, maxPrice,keyword, sort);
    }

    public List<Book> getTop5Newest() {
        return bookRepository.findTop5ByOrderByIdDesc();
    }

    public List<Book> getTop5BestSellers() {
        return bookRepository.findTop5BestSellers();
    }

    public List<Book> getTop5ByCategory(Long categoryId) {
        return bookRepository.findTop5ByCategoryIdOrderByIdDesc(categoryId);
    }

    public List<Book> searchLive(String keyword) {
        return bookRepository.findTop5ByNameContainingIgnoreCaseOrderByIdDesc(keyword);
    }

    public List<Book> getTop5Manga() {
        return bookRepository.findTop5ByName("MANGA");
    }
}