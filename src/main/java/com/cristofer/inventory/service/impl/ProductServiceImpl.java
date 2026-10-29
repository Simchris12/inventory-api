package com.cristofer.inventory.service.impl;

import com.cristofer.inventory.dto.request.ProductRequest;
import com.cristofer.inventory.dto.response.ProductResponse;
import com.cristofer.inventory.entity.Product;
import com.cristofer.inventory.exception.DuplicateResourceException;
import com.cristofer.inventory.exception.ResourceNotFoundException;
import com.cristofer.inventory.mapper.ProductMapper;
import com.cristofer.inventory.repository.ProductRepository;
import com.cristofer.inventory.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse create(ProductRequest request) {

        if (productRepository.existsByName(request.getName())){
            throw new DuplicateResourceException("Product already exists with name: " + request.getName());
        }

        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> findAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable)
                    .map(productMapper::toResponse);
        }
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if(!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName())){
            throw new DuplicateResourceException("Product already exists with name: " + request.getName());
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product updateProduct = productRepository.save(product);

        return productMapper.toResponse(updateProduct);
    }


    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }
}
