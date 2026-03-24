package com.example.demowebshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private double price;
    private int stock;
    private String image;

    @OneToMany(mappedBy = "book")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "book")
    private List<CartItem> cartItems;
}

