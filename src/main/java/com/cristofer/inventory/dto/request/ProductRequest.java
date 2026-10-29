package com.cristofer.inventory.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    //Datos que vamos a aceptar del cliente
    //name of the product
    @NotBlank
    @Size(min = 3, max = 100)
    String name;

    //price of the product
    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = true,
            message = "El precio no puede ser negativo.")
    BigDecimal price;

    //stock of the product
    @NotNull(message = "El stock es obligatorio.")
    @PositiveOrZero(message = "El stock no puede ser negativo.")
    Integer stock;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
}


