package com.cristofer.inventory.service.impl;

import com.cristofer.inventory.dto.request.ProductRequest;
import com.cristofer.inventory.dto.response.ProductResponse;
import com.cristofer.inventory.entity.Category;
import com.cristofer.inventory.entity.Product;
import com.cristofer.inventory.exception.DuplicateResourceException;
import com.cristofer.inventory.exception.ResourceNotFoundException;
import com.cristofer.inventory.mapper.CategoryMapper;
import com.cristofer.inventory.mapper.ProductMapper;
import com.cristofer.inventory.repository.CategoryRepository;
import com.cristofer.inventory.repository.ProductRepository;
import com.cristofer.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Product already exists with name: " + request.getName());
        }

        //Validar y obtener la categoría de la base de datos
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        //Mapear y asignar la categoría
        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public Page<ProductResponse> findAll(String name, Pageable pageable) {
        Page<Product> productsPage;
        if (name != null && !name.trim().isEmpty()) {
            productsPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            productsPage = productRepository.findAll(pageable);
        }
        return productsPage.map(productMapper::toResponse);
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Product already exists with name: " + request.getName());
        }

        //Validar y obtener la nueva categoría
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        //Actualizar datos básicos y la categoría
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
