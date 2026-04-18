package com.example.demowebshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {
    private String discountCode;
    private String shippingCode;

    // khách vãng lai
    private String customerName;
    private String phoneNumber;
    private String shippingAddress;

    private String paymentMethod;
}
