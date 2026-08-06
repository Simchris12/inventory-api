package com.cristofer.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    //Id of the product
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //name of the product
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    //price of the product
    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = true,
            message = "El precio no puede ser negativo.")
    private BigDecimal price;

    //stock of the product
    @NotNull(message = "El stock es obligatorio.")
    @PositiveOrZero(message = "El stock no puede ser negativo.")
    private Integer stock;


}
