package com.github.techChallenge.domain.user.dto;

import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.userlevel.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UserUpdateInputDTO(

        @Schema(
                description = "Nome completo do usuário",
                example = "José da Silva",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O nome é obrigatório")
        @Size(
                max = 50,
                message = "O nome deve possuir no máximo 50 caracteres."
        )
        String name,

        @Schema(
                description = "E-mail do usuário",
                example = "jose.silva@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail possui formato inválido.")
        @Size(
                max = 255,
                message = "O e-mail deve possuir no máximo 255 caracteres."
        )
        String email,

        @Schema(
                description = "Nome de usuário para acesso ao sistema",
                example = "jose.silva",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O login é obrigatório.")
        @Size(
                min = 4,
                max = 100,
                message = "O login deve possuir entre 4 e 100 caracteres."
        )
        String login,

        @Schema(
                description = "Endereço do usuário",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O endereço é obrigatório.")
        @Valid
        Address address

) {
}