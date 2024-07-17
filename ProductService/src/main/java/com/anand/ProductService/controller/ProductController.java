package com.anand.ProductService.controller;

import com.anand.ProductService.model.ProductRequest;
import com.anand.ProductService.model.ProductResponse;
import com.anand.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
       long productId =  productService.addProduct(productRequest);

       return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long id){
        ProductResponse productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") long productId,
            @RequestParam long quantity
    ){
        productService.reduceQuantity(productId,quantity);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
