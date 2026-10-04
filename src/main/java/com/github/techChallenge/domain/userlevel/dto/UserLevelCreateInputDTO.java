package com.github.techChallenge.domain.userlevel.dto;

import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.userlevel.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserLevelCreateInputDTO(

        @Schema(
                description = "Título do nível",
                example = "Proprietário"
        )
        @NotBlank(message = "O título é obrigatório.")
        @Size(
                max = 50,
                message = "O título deve possuir no máximo 50 caracteres."
        )
        String title) {
}