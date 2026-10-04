package com.github.techChallenge.domain.user.dto;

import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.userlevel.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserCreateOutputDTO(
    @Schema(description = "ID do usuário, gerado durante a criação", example = "1", format= "integer")
    Long id,
    @Schema(description = "Nome completo do usuário", example = "José da Silva", format= "string")
    String name,
    @Schema(description = "Nome de usuário de acesso ao sistema", example = "jose.silva", format= "string")
    String login,
    @Schema(description = "Nivel de acesso deste usuário.", format= "string")
    UserLevel level,
    @Schema(description = "Endereço do usuário")
    Address address,
    @Schema(description = "Data e hora do momento da criação, no formato YYYY-MM-DD HH:mm:ss", example = "2026-07-29 19:04:23", format= "string")
    LocalDateTime createdAt) {
}
