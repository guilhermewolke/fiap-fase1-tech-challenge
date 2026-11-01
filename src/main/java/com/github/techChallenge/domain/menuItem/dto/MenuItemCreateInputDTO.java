package com.github.techChallenge.domain.menuItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record MenuItemCreateInputDTO(

        @Schema(description = "Nome do item do cardápio", example = "X-Burger", maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Descrição do item", example = "Pão, hambúrguer e queijo", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
        String description,

        @Schema(description = "Preço do item", example = "29.90", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal price,

        @Schema(description = "Indica se o item só pode ser pedido no restaurante", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        Boolean dineInOnly,

        @Schema(description = "Caminho da foto do prato", example = "/fotos/x-burger.png", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
        String photoPath) {
}

