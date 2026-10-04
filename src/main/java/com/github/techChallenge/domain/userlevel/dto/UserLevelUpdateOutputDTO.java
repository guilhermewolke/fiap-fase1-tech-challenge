package com.github.techChallenge.domain.userlevel.dto;

import com.github.techChallenge.domain.user.Address;
import com.github.techChallenge.domain.userlevel.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserLevelUpdateOutputDTO(
    @Schema(description = "ID do nível do usuário", example = "1", format= "integer")
    int id,
    @Schema(description = "Título do nível do usuário", example = "Proprietário", format= "string")
    String title) {
}
