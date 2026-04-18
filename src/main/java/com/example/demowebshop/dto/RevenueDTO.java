package com.example.demowebshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RevenueDTO {
    private String label; // Chứa nhãn trục X trên biểu đồ
    private BigDecimal revenue;

    // Constructor cho nhóm theo Ngày
    public RevenueDTO(Integer day, Integer month, Integer year, BigDecimal revenue) {
        this.label = String.format("%02d/%02d/%04d", day, month, year);
        this.revenue = revenue;
    }

    // Constructor cho nhóm theo Tháng
    public RevenueDTO(Integer month, Integer year, BigDecimal revenue) {
        this.label = String.format("Tháng %02d/%04d", month, year);
        this.revenue = revenue;
    }

    // Constructor cho nhóm theo Năm
    public RevenueDTO(Integer year, BigDecimal revenue) {
        this.label = String.valueOf(year);
        this.revenue = revenue;
    }
}