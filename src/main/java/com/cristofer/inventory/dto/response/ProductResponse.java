package com.cristofer.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload representing a product in the inventory")
public class ProductResponse {

    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Mechanical Keyboard RGB")
    private String name;

    @Schema(description = "Detailed description of the product", example = "Blue switches, aluminum frame")
    private String description;

    @Schema(description = "Unit price of the product", example = "89.99")
    private BigDecimal price;

    @Schema(description = "Available units in stock", example = "25")
    private Integer stock;

    @Schema(description = "Timestamp when the product was created", example = "2026-10-29T11:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the product was last updated", example = "2026-10-29T14:20:10")
    private LocalDateTime updatedAt;

}
