package com.example.demowebshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CartItemDTO {
    private Long id;           // OrderDetail ID (or session-based ID for guests)
    private Long bookId;
    private String bookName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal finalPrice;
    private Boolean isSessionBased;  // true if from session, false if from database
}
