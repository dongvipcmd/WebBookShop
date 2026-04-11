package com.example.demowebshop.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    // "PERCENTAGE" | "FIXED" | "NONE"
    private String discountType;

    // Số tiền giảm cố định — dùng khi discountType = "FIXED"
    private BigDecimal discountValue;

    // Phần trăm giảm — dùng khi discountType = "PERCENTAGE"
    private BigDecimal discountPercent;

    // Mức giảm tối đa khi áp dụng theo %
    private BigDecimal maxDiscount;

    // true = miễn phí ship
    private Boolean freeShipping = false;

    // Phí ship tối đa được miễn — null = miễn toàn bộ
    private BigDecimal maxShippingDiscount;

    // Giá trị đơn hàng tối thiểu để áp dụng
    private BigDecimal minOrderValue = BigDecimal.ZERO;

    // Tổng số lượt dùng tối đa
    private Integer totalQuantity;

    // Số lượt đã dùng
    private Integer usedQuantity = 0;

    private LocalDate startDate;
    private LocalDate endDate;

    // true = đang hoạt động
    private Boolean active = true;
}
