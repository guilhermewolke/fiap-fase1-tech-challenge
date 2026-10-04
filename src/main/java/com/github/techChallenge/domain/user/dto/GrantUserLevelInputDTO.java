package com.github.techChallenge.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GrantUserLevelInputDTO(
        @Schema(
                description = "ID do usuário",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O ID do usuário é obrigatório")
        Long userID,

        @Schema(
                description = "ID do nível de usuário",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O ID do nível de usuário é obrigatório.")
        int userLevelID
) {
}