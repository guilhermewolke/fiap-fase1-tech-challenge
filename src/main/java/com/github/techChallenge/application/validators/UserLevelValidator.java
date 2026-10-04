package com.github.techChallenge.application.validators;

import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.application.usecases.userlevel.UserLevelUseCase;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;

public class UserLevelValidator extends UserLevelUseCase {

    public UserLevelValidator(IUserLevelGateway gateway, IUserLevelMapper mapper){
        super(gateway, mapper);
    }

    public boolean titleExists(String title, int id) {
        return this.gateway.titleExists(title, id);
    }

    public boolean titleExists(String title) {
        return this.gateway.titleExists(title);
    }

    public boolean existsByID(int id) {return this.gateway.existById(id);}

}
