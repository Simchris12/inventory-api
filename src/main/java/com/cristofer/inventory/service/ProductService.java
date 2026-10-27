package com.cristofer.inventory.service;

import com.cristofer.inventory.dto.request.ProductRequest;
import com.cristofer.inventory.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    // Method to create a new product
    ProductResponse create(ProductRequest request);

    // Method to Find a product by its ID
    ProductResponse findById(Long id);
    // Method to Find all products
    List<ProductResponse> findAll();
    // Method to update a product by its ID
    ProductResponse update(Long id, ProductRequest request);
    // Method to delete a product by its ID
    void delete(Long id);
}
