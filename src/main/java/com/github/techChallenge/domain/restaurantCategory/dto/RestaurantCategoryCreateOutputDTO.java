package com.github.techChallenge.domain.restaurantCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RestaurantCategoryCreateOutputDTO(
    @Schema(description = "ID do tipo de restaurante, gerado durante a criação", example = "1", format= "integer")
    int id,
    @Schema(description = "Título do tipo de restaurante", example = "Lanches", format= "string")
    String title) {
}
