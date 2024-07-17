package com.anand.ProductService.service;

import com.anand.ProductService.entity.Product;
import com.anand.ProductService.model.ProductRequest;
import com.anand.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);
    ProductResponse getProductById(long id);

    void reduceQuantity(long productId, long quantity);
}
