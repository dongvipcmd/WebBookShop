package com.example.demowebshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK trỏ tới orders.id
    private Long orderId;

    // FK trỏ tới book.id
    private Long bookId;

    private Integer quantity;

    // Giá tại thời điểm mua — tránh thay đổi khi admin cập nhật giá sách
    private BigDecimal unitPrice;

    // Thành tiền = unitPrice * quantity — lưu sẵn để tránh tính lại
    private BigDecimal finalPrice;
}