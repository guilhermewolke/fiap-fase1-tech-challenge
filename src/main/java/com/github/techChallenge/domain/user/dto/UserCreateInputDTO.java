package com.github.techChallenge.domain.user.dto;

import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.userlevel.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateInputDTO(

        @Schema(
                description = "Nome completo do usuário",
                example = "José da Silva"
        )
        @NotBlank(message = "O nome é obrigatório.")
        @Size(
                max = 50,
                message = "O nome deve possuir no máximo 50 caracteres."
        )
        String name,

        @Schema(
                description = "E-mail do usuário",
                example = "jose.silva@email.com"
        )
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail possui formato inválido.")
        @Size(
                max = 255,
                message = "O e-mail deve possuir no máximo 255 caracteres."
        )
        String email,

        @Schema(
                description = "Login do usuário",
                example = "jose.silva"
        )
        @NotBlank(message = "O login é obrigatório.")
        @Size(
                min = 4,
                max = 100,
                message = "O login deve possuir entre 4 e 100 caracteres."
        )
        String login,

        @Schema(
                description = "Senha do usuário",
                example = "Senha@123",
                accessMode = Schema.AccessMode.WRITE_ONLY
        )
        @NotBlank(message = "A senha é obrigatória.")
        @Size(
                min = 8,
                max = 100,
                message = "A senha deve possuir entre 8 e 100 caracteres."
        )
        String password,

        @Schema(description = "Endereço do usuário")
        @NotNull(message = "O endereço é obrigatório.")
        @Valid
        Address address

) {
}