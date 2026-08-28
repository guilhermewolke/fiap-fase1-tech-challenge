package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.exceptions.DuplicateEmailException;
import com.github.techChallenge.application.exceptions.DuplicateLoginException;
import com.github.techChallenge.application.exceptions.UserNotFoundException;
import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.application.gateways.UserGateway;
import com.github.techChallenge.application.validators.UserValidator;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.domain.user.dto.UserUpdateInputDTO;
import com.github.techChallenge.infrastructure.entities.user.UserEntity;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;

import java.util.Optional;

public class UpdateUserUseCase extends UserUseCase {
    private final UserValidator userValidator;

    public UpdateUserUseCase(IUserGateway gateway, IUserMapper mapper, UserValidator userValidator) {
        super(gateway, mapper);
        this.userValidator = userValidator;
    }

    public UserOutputDTO execute(UserUpdateInputDTO dto, Long id) {
        if (!this.gateway.existById(id)) throw new UserNotFoundException("Usuário não encontrado");

        if (this.userValidator.emailExists(dto.email(), id))
            throw new DuplicateEmailException(dto.email());

        if (this.userValidator.loginExists(dto.login(), id))
            throw new DuplicateLoginException(dto.login());

        User user = new User();
        user.update(dto.name(), dto.email(), dto.login(), dto.level(), dto.address());

        user = this.gateway.update(user, id);
        return this.mapper.fromDomainToOutputDTO(user);
    }
}
