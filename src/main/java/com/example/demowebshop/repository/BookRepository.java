package com.example.demowebshop.repository;

import com.example.demowebshop.entity.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b " +
            "LEFT JOIN FETCH b.category " +
            "LEFT JOIN FETCH b.author " +
            "WHERE (:categoryId IS NULL OR b.categoryId = :categoryId) AND " +
            "(:authorId IS NULL OR b.authorId = :authorId) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
            "(:keyword IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Book> filterBooks(@Param("categoryId") Long categoryId,
                           @Param("authorId") Long authorId,
                           @Param("maxPrice") BigDecimal maxPrice,
                           @Param("keyword") String keyword,
                           Sort sort);

    List<Book> findTop5ByNameContainingIgnoreCaseOrderByIdDesc(String keyword);

    List<Book> findTop5ByOrderByIdDesc();

    // Lấy 5 sách theo ID thể loại
    List<Book> findTop5ByCategoryIdOrderByIdDesc(Long categoryId);

    // Lấy 5 sách bán chạy nhất (Tính tổng số lượng trong order_detail)
    @Query(value = "SELECT b.* FROM book b " +
            "LEFT JOIN order_detail od ON b.id = od.book_id " +
            "GROUP BY b.id " +
            "ORDER BY SUM(od.quantity) DESC LIMIT 5", nativeQuery = true)
    List<Book> findTop5BestSellers();

    //Lấy 5 cuốn sách bởi tên thể loại
    @Query(value = """
    SELECT b.*
    FROM book b
    JOIN category c ON b.category_id = c.id
    WHERE LOWER(c.name) = LOWER(:name)
    ORDER BY b.id DESC
    LIMIT 5
""", nativeQuery = true)
    List<Book> findTop5ByName(@Param("name") String name);
}