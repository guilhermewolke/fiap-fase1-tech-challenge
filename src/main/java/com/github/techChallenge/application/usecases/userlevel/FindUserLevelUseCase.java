package com.github.techChallenge.application.usecases.userlevel;

import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;

public class FindUserLevelUseCase extends UserLevelUseCase {

    public FindUserLevelUseCase(IUserLevelGateway gateway, IUserLevelMapper mapper) {
        super(gateway, mapper);
    }

    public UserLevelOutputDTO execute(int id) {
        UserLevel userLevel = this.gateway.find(id);
        return this.mapper.fromDomainToOutputDTO(userLevel);
    }
}
