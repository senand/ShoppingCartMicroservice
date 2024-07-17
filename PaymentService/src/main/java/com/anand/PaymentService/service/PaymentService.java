package com.anand.PaymentService.service;

import com.anand.PaymentService.model.PaymentRequest;
import com.anand.PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
