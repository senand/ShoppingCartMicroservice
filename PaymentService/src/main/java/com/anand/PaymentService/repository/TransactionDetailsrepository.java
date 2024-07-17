package com.anand.PaymentService.repository;

import com.anand.PaymentService.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailsrepository extends JpaRepository<TransactionDetails,Long> {
    TransactionDetails findByOrderId(long orderId);
}
