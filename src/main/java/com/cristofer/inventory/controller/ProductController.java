package com.cristofer.inventory.controller;

import com.cristofer.inventory.dto.request.ProductRequest;
import com.cristofer.inventory.dto.response.ProductResponse;
import com.cristofer.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "products", description = "CRUD operations for the inventory management.")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Register a new product in the system. " +
            "The name of the product must be unique to proceed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data. (Example: negative price.)"),
            @ApiResponse(responseCode = "409", description = "Conflict: A product with the same name already exist.")
    })
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request){
        ProductResponse response = productService.create(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Returns a list of all products registered in the inventory.")
    @ApiResponse(responseCode = "200", description = "List of products successfully retrieved.")
    public ResponseEntity<List<ProductResponse>> findAll(){
        List<ProductResponse> responses = productService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a product by ID", description = "Retrieves and returns the details of a specific " +
            "product using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id){
        ProductResponse response = productService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update the details of an existing product. The new name must" +
            " not match that of another product already registered.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "409", description = "Conflict: Another product with that name already exists.")
    })
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request){
        ProductResponse response = productService.update(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Physically remove a product from the inventory using its" +
            " unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully removed. (No content)."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
