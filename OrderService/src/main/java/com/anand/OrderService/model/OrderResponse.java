package com.anand.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private String orderStatus;
    private Instant orderDate;
    private  long amount;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetails {
        private String productName;
        private long productId;
        private long quantity;
        private long price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentDetails {
        private long paymentId;
        private String paymentStatus;
        private long amount;
        private PaymentMode paymentMode;
        private Instant paymentDate;
        private long orderId;
    }
}
