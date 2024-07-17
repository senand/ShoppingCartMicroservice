package com.anand.PaymentService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private long paymentId;
    private String paymentStatus;
    private long amount;
    private PaymentMode paymentMode;
    private Instant paymentDate;
    private long orderId;
}
