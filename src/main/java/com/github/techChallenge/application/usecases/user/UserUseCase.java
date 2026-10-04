package com.github.techChallenge.application.usecases.user;

import com.github.techChallenge.application.gateways.user.IUserGateway;
import com.github.techChallenge.domain.user.IUserMapper;

public class UserUseCase {
    protected final IUserGateway gateway;
    protected final IUserMapper mapper;

    public UserUseCase(IUserGateway gateway, IUserMapper mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

}
