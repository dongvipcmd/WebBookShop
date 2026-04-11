package com.example.demowebshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK trỏ tới order.id
    private Long orderId;

    // FK trỏ tới book.id
    private Long bookId;

    private Integer quantity;

    // Lưu giá tại thời điểm mua để tránh thay đổi khi giá SP cập nhật
    private BigDecimal unitPrice;
}
