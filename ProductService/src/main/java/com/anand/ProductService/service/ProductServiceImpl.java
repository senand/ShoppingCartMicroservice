package com.anand.ProductService.service;

import com.anand.ProductService.entity.Product;
import com.anand.ProductService.exception.ProductServiceCustomException;
import com.anand.ProductService.model.ProductRequest;
import com.anand.ProductService.model.ProductResponse;
import com.anand.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product");
        Product product =
                Product.builder()
                        .productName(productRequest.getName())
                        .quantity(productRequest.getQuantity())
                        .price(productRequest.getPrice())
                        .build();
        productRepository.save(product);

        log.info("Product Created");

        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long id) {
        log.info("Get the product for productId: {} ",id);
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductServiceCustomException("Product with given id is not found","Product Not Found"));

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for id: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("Product with given id is Not FOund",
                        "PRODUCT_NOT_FOUND"));

        if(product.getQuantity() < quantity){
            throw new ProductServiceCustomException("Product does not have suffiecent Quantity",
                    "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        log.info("Product Quantity Updated Succesfully");
    }
}
