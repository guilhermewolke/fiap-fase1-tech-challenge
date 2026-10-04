package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.gateways.user.IUserGateway;
import com.github.techChallenge.domain.user.IUserMapper;

public class DeleteUserUseCase extends UserUseCase {

    public DeleteUserUseCase(IUserGateway gateway, IUserMapper mapper) {
        super(gateway, mapper);
    }

    public void execute(Long id) {
        this.gateway.delete(id);
    }
}
