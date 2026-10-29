package com.cristofer.inventory.service.impl;

import com.cristofer.inventory.dto.request.ProductRequest;
import com.cristofer.inventory.dto.response.ProductResponse;
import com.cristofer.inventory.entity.Product;
import com.cristofer.inventory.exception.ResourceNotFoundException;
import com.cristofer.inventory.mapper.ProductMapper;
import com.cristofer.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita Mockito en esta clase
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository; // Simulacro de la BD

    @Mock
    private ProductMapper productMapper; // Simulacro del Mapper

    @InjectMocks
    private ProductServiceImpl productService; // La clase REAL que estamos probando

    @Test
    void findById_ShouldReturnProductResponse_WhenProductExists() {
        // 1. ARRANGE (Preparar los datos)
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("Gaming Mouse");
        mockProduct.setPrice(new BigDecimal("50.00"));
        mockProduct.setStock(10);

        ProductResponse mockResponse = new ProductResponse();
        mockResponse.setId(productId);
        mockResponse.setName("Gaming Mouse");
        mockResponse.setPrice(new BigDecimal("50.00"));
        mockResponse.setStock(10);

        // Comportamiento esperado de los mocks
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(productMapper.toResponse(mockProduct)).thenReturn(mockResponse);

        // 2. ACT (Ejecutar el método)
        ProductResponse result = productService.findById(productId);

        // 3. ASSERT (Verificar resultados)
        assertNotNull(result); // El resultado no debe ser nulo
        assertEquals("Gaming Mouse", result.getName()); // El nombre debe coincidir
        assertEquals(new BigDecimal("50.00"), result.getPrice()); // El precio debe coincidir

        // Verifica que el repositorio realmente fue llamado exactamente 1 vez
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenProductDoesNotExist() {
        // 1. ARRANGE
        Long nonExistentId = 99L;

        // Simulamos que el repositorio no encuentra nada
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // 2 & 3. ACT & ASSERT
        // Usamos assertThrows para verificar que el método lance la excepción correcta
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(nonExistentId);
        });

        // Verificamos que el repositorio fue consultado 1 vez
        verify(productRepository, times(1)).findById(nonExistentId);

        // Verificamos que el mapper NUNCA fue llamado (porque falló antes)
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void create_ShouldReturnProductResponse_WhenNameIsUnique() {
        // 1. ARRANGE
        // Datos de entrada del cliente
        ProductRequest request = new ProductRequest();
        request.setName("Mechanical Keyboard");
        request.setPrice(new BigDecimal("120.00"));
        request.setStock(15);

        // Entidad que devuelve el mapper (sin ID aún)
        Product productEntity = new Product();
        productEntity.setName("Mechanical Keyboard");
        productEntity.setPrice(new BigDecimal("120.00"));
        productEntity.setStock(15);

        // Entidad que devuelve la base de datos (ya con ID)
        Product savedProduct = new Product();
        savedProduct.setId(2L);
        savedProduct.setName("Mechanical Keyboard");
        savedProduct.setPrice(new BigDecimal("120.00"));
        savedProduct.setStock(15);

        // Respuesta final que devuelve el mapper
        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setId(2L);
        expectedResponse.setName("Mechanical Keyboard");
        expectedResponse.setPrice(new BigDecimal("120.00"));
        expectedResponse.setStock(15);

        // Configuramos el comportamiento de los simulacros
        when(productRepository.existsByName(request.getName())).thenReturn(false); // Simulamos que el nombre NO existe
        when(productMapper.toEntity(request)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(expectedResponse);

        // 2. ACT
        ProductResponse result = productService.create(request);

        // 3. ASSERT
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Mechanical Keyboard", result.getName());

        // Verificamos que se llamó al repositorio para comprobar el nombre y para guardar
        verify(productRepository, times(1)).existsByName(request.getName());
        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    void update_ShouldReturnProductResponse_WhenDataIsValid() {
        Long productId = 1L;
        ProductRequest request = new ProductRequest();
        request.setName("New Keyboard");
        request.setPrice(new BigDecimal("150.00"));
        request.setStock(20);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Keyboard");

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("New Keyboard");

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setId(productId);
        expectedResponse.setName("New Keyboard");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        // Simulamos que el nuevo nombre no existe en la base de datos
        when(productRepository.existsByName(request.getName())).thenReturn(false);
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);
        when(productMapper.toResponse(updatedProduct)).thenReturn(expectedResponse);

        ProductResponse result = productService.update(productId, request);

        assertNotNull(result);
        assertEquals("New Keyboard", result.getName());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void delete_ShouldDeleteProduct_WhenProductExists() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        // doNothing() se usa para métodos void (como el delete del repositorio)
        doNothing().when(productRepository).delete(existingProduct);

        productService.delete(productId);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(existingProduct);
    }

    @Test
    void findAll_ShouldReturnPaginatedProducts_WhenNoNameFilterIsProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        ProductResponse response = new ProductResponse();

        // Simulamos una página con 1 producto
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponse(product)).thenReturn(response);

        // Pasamos null como filtro de nombre
        Page<ProductResponse> result = productService.findAll(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }
}