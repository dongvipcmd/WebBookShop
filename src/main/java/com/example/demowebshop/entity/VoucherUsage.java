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
                // Mỗi khách chỉ được dùng 1 mã voucher 1 lần
                columnNames = {"voucherId", "userId"}
        )
)
public class VoucherUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK trỏ tới voucher.id
    private Long voucherId;

    // FK trỏ tới khachhang.id
    private Long userId;

    // FK trỏ tới donhang.id — mỗi đơn chỉ có 1 bản ghi usage
    @Column(unique = true)
    private Long orderId;

    private LocalDateTime usedAt = LocalDateTime.now();

    // Số tiền giảm giá thực tế đã áp dụng
    private BigDecimal discountApplied = BigDecimal.ZERO;

    // Phí ship thực tế đã được miễn
    private BigDecimal shippingDiscountApplied = BigDecimal.ZERO;
}