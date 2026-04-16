package com.example.demowebshop.repository;

import com.example.demowebshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c.* FROM category c " +
            "JOIN book b ON c.id = b.category_id " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(b.id) DESC LIMIT 3", nativeQuery = true)
    List<Category> findTop3CategoriesWithMostBooks();
}
