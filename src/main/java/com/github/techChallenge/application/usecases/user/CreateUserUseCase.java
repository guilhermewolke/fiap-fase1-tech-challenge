package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.gateways.IUserGateway;
import com.github.techChallenge.domain.user.IUserMapper;
import com.github.techChallenge.domain.user.User;
import com.github.techChallenge.domain.user.dto.UserCreateInputDTO;
import com.github.techChallenge.domain.user.dto.UserOutputDTO;
import com.github.techChallenge.application.gateways.UserGateway;
import com.github.techChallenge.infrastructure.security.ISecurityConfig;
import com.github.techChallenge.shared.EmailAlreadyExistsException;
import com.github.techChallenge.shared.LoginAlreadyExistsException;

public class CreateUserUseCase extends UserUseCase {
    private final ISecurityConfig security;
    public CreateUserUseCase(IUserGateway gateway, IUserMapper mapper, ISecurityConfig security) {
        super(gateway, mapper);
        this.security = security;
    }

    public UserOutputDTO execute(UserCreateInputDTO dto) {

        User user = User.create(
                dto.name(),
                dto.email(),
                dto.login(),
                security.passwordEncoder(dto.password(), dto.login()),
                dto.level(),
                dto.address());

        if (gateway.emailExists(user.getEmail(), user.getId()))
            throw new EmailAlreadyExistsException();

        if (gateway.loginExists(user.getLogin()))
            throw new LoginAlreadyExistsException();

        user = this.gateway.create(user);
        return this.mapper.fromDomainToOutputDTO(user);
    }
}
