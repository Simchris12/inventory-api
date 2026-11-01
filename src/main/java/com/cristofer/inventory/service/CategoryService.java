package com.cristofer.inventory.service;

import com.cristofer.inventory.dto.request.CategoryRequest;
import com.cristofer.inventory.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    List<CategoryResponse> findAll();

    CategoryResponse findById(Long id);

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}