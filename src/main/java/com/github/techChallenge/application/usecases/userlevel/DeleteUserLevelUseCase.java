package com.github.techChallenge.application.usecases.userlevel;

import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;

public class DeleteUserLevelUseCase extends UserLevelUseCase {

    public DeleteUserLevelUseCase(IUserLevelGateway gateway, IUserLevelMapper mapper) {
        super(gateway, mapper);
    }

    public void execute(int id) {
        this.gateway.delete(id);
    }
}
