package com.example.demowebshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer stockQuantity;

    // FK trỏ tới category.id
    @Column(name = "category_id")
    private Long categoryId;

    // FK authorId
    @Column(name = "author_id")
    private Long authorId;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;

    @Formula("""
    (SELECT COALESCE(SUM(od.quantity), 0)
     FROM order_detail od
     JOIN orders o ON od.order_id = o.id
     WHERE od.book_id = id
       AND o.status = 'PAID')
""")
    private Integer soldQuantity;

}

