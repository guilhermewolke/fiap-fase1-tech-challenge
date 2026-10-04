package com.github.techChallenge.domain.userlevel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLevelCreateOutputDTO(
    @Schema(description = "ID do nível do usuário, gerado durante a criação", example = "1", format= "integer")
    int id,
    @Schema(description = "Título do nível", example = "Proprietário", format= "string")
    String title) {
}
