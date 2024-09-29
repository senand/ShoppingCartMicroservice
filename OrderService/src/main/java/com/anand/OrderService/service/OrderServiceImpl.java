package com.anand.OrderService.service;

import com.anand.OrderService.entity.Order;
import com.anand.OrderService.exception.CustomException;
import com.anand.OrderService.external.client.PaymentService;
import com.anand.OrderService.external.client.ProductService;
import com.anand.OrderService.external.client.request.PaymentRequest;
import com.anand.OrderService.model.OrderRequest;
import com.anand.OrderService.model.OrderResponse;
import com.anand.OrderService.model.PaymentResponse;
import com.anand.OrderService.model.ProductResponse;
import com.anand.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class    OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing the order: {}",orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("Creating Order with Status CREATED");
        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .orderStatus("CREATED")
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .amount(orderRequest.getTotalAmount())
                .build();
        order = orderRepository.save(order);
        log.info("Calling PaymentService to create the Payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Order Placed Successfully. Changing Order Status to Placed");
            orderStatus = "PLACED";
        }catch (Exception e){
            log.error("Error occured in payment. Changing order status to Failed");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Placed Successfully with Order Id: {}",order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for orderId: {}",orderId);
       Order order = orderRepository.findById(orderId)
               .orElseThrow(()->new CustomException("Order Not Found for OrderId", "NOT_FOUND",404));

       log.info("Invoking ProductService to get Product Details for productId: {}",order.getProductId());
        ProductResponse productResponse=
                restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(),
                        ProductResponse.class);

        log.info("Getting Payment information from Payment Service");

        PaymentResponse paymentResponse=
                restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(),
                        PaymentResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getPaymentStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();


       OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
               .paymentDetails(paymentDetails)
                .build();

        return orderResponse;

    }
}
