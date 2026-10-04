package com.github.techChallenge.application.usecases.userlevel;

import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;

public class UserLevelUseCase {
    protected final IUserLevelGateway gateway;
    protected final IUserLevelMapper mapper;

    public UserLevelUseCase(IUserLevelGateway gateway, IUserLevelMapper mapper) {
        this.gateway = gateway;
        this.mapper = mapper;
    }

}
