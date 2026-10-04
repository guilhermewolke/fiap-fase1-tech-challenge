package com.github.techChallenge.domain.restaurantCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestaurantCategoryCreateInputDTO(

        @Schema(
                description = "Título do tipo de restaurante",
                example = "Lanches"
        )
        @NotBlank(message = "O título é obrigatório.")
        @Size(
                max = 50,
                message = "O título deve possuir no máximo 50 caracteres."
        )
        String title) {
}