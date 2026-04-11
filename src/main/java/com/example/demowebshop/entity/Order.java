package com.example.demowebshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDateTime = LocalDateTime.now();

    // FK trỏ tới user.id
    private Long userId;

    // có thể null nếu ko dùng voucher
    private Long voucherId;

    // Tổng tiền hàng trước giảm và cộng ship
    private BigDecimal totalAmount;

    // Số tiền được giảm từ voucher
    private BigDecimal discountAmount = BigDecimal.ZERO;

    // Phí vận chuyển (0 nếu dùng voucher free ship)
    private BigDecimal shippingFee = BigDecimal.ZERO;

    // Tổng thanh toán = totalAmount - discountAmount + shippingFee
    private BigDecimal finalAmount;

}