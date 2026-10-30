package com.cristofer.inventory.mapper;

import com.cristofer.inventory.dto.request.ProductRequest;
import com.cristofer.inventory.dto.response.ProductResponse;
import com.cristofer.inventory.entity.Product;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        return product;
    }


    public ProductResponse toResponse(Product product){
        if (product == null){
            return null;
        }
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }
}