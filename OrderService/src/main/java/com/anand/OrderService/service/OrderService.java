package com.anand.OrderService.service;

import com.anand.OrderService.model.OrderRequest;
import com.anand.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
