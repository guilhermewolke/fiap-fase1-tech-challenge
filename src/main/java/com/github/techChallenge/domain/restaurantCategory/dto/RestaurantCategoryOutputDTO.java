package com.github.techChallenge.domain.restaurantCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RestaurantCategoryOutputDTO(

        @Schema(description = "ID do tipo de restaurante", example = "1")
        int id,

        @Schema(description = "Título do tipo de restaurante", example = "Lanches")
        String title) {
}