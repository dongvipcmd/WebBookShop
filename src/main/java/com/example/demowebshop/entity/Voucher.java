package com.example.demowebshop.entity;
import com.example.demowebshop.enums.DiscountType;
import com.example.demowebshop.enums.VoucherType;
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

    // "DISCOUNT" = giảm giá hàng | "SHIPPING" = giảm ship
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountPercent;
    private BigDecimal maxDiscount;


    private BigDecimal maxShippingDiscount;  // null = miễn toàn bộ ship

    // Điều kiện chung
    private BigDecimal minOrderValue = BigDecimal.ZERO;
    private Integer totalQuantity; // số lần sử dụng
    private Integer usedQuantity = 0;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active = true;
}
