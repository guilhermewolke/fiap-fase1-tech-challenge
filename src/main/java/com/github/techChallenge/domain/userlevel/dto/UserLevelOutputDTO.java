package com.github.techChallenge.domain.userlevel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLevelOutputDTO(

        @Schema(description = "ID do nível do usuário", example = "1")
        int id,

        @Schema(description = "Título do nível do usuário", example = "Proprietário")
        String title) {
}