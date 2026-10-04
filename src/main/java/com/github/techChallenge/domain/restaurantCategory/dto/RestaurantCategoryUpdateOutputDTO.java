package com.github.techChallenge.domain.restaurantCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RestaurantCategoryUpdateOutputDTO(
    @Schema(description = "ID do tipo de restaurante", example = "1", format= "integer")
    int id,
    @Schema(description = "Título do tipo do restaurante", example = "Lanches", format= "string")
    String title) {
}
