package com.example.demowebshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "voucher_usage",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_voucher_user",
                // Mỗi khách chỉ dùng 1 mã 1 lần
                columnNames = {"voucherId", "userId"}
        )
)
public class VoucherUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK trỏ tới voucher.id
    private Long voucherId;

    // FK trỏ tới user.id
    private Long userId;

    // FK trỏ tới order.id
    // (1 discount voucher + 1 shipping voucher)
    private Long orderId;

    private LocalDateTime usedAt = LocalDateTime.now();

    // Số tiền thực tế đã giảm của voucher này
    private BigDecimal discountApplied = BigDecimal.ZERO;
}