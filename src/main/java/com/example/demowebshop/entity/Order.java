package com.example.demowebshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime orderDate = LocalDateTime.now();

    // Mã giảm giá sản phẩm
    private Long discountVoucherId;

    // Mã giảm phí ship
    private Long shippingVoucherId;

    private BigDecimal totalAmount;       // tổng tiền hàng
    private BigDecimal discountAmount;    // tiền giảm từ discountVoucher
    private BigDecimal shippingFee;       // phí ship gốc
    private BigDecimal shippingDiscount;  // ship được giảm từ shippingVoucher
    private BigDecimal finalAmount;       // tổng thanh toán cuối

    private String status = "PENDING";
}