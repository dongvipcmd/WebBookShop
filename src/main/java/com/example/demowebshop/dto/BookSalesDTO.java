package com.example.demowebshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookSalesDTO {
    private String bookName;
    private Long totalQuantity;
}
