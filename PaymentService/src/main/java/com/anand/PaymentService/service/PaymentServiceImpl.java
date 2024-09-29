package com.anand.PaymentService.service;

import com.anand.PaymentService.entity.TransactionDetails;
import com.anand.PaymentService.model.PaymentMode;
import com.anand.PaymentService.model.PaymentRequest;
import com.anand.PaymentService.model.PaymentResponse;
import com.anand.PaymentService.repository.TransactionDetailsrepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private TransactionDetailsrepository transactionDetailsrepository;
    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording Payment Details:{}",paymentRequest);

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentStatus("SUCCESS")
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentDate(Instant.now())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .orderId(paymentRequest.getOrderId())
                .build();

        transactionDetailsrepository.save(transactionDetails);
        log.info("Transaction Completed with id: {}",transactionDetails.getId());
        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
        log.info("Getting Payment Details for the Order Id: {}",orderId);
        log.info("Getting All Payment Details for the Order Id: {}",transactionDetailsrepository.findAll());
        TransactionDetails transactionDetails = transactionDetailsrepository.findByOrderId(Long.valueOf(orderId));
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentStatus(transactionDetails.getPaymentStatus())
                .amount(transactionDetails.getAmount())
                .paymentDate(transactionDetails.getPaymentDate())
                .build();
        return paymentResponse;
    }
}
