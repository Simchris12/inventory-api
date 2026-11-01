package com.cristofer.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload representing a product category")
public class CategoryResponse {

    @Schema(description = "Unique identifier of the category", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Peripherals")
    private String name;

    @Schema(description = "Category description", example = "Keyboards, mice, headsets, and external devices")
    private String description;

    @Schema(description = "Timestamp when the category was created", example = "2026-10-31T23:50:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the category was last updated", example = "2026-10-31T23:55:00")
    private LocalDateTime updatedAt;
}