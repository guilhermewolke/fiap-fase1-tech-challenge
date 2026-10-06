package com.github.techChallenge.application.usecases.userlevel;

import com.github.techChallenge.application.exceptions.DuplicateUserLevelTitleException;
import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.application.validators.UserLevelValidator;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.domain.userlevel.dto.UserLevelCreateInputDTO;
import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;

public class CreateUserLevelUseCase extends UserLevelUseCase {

    private final UserLevelValidator validator;

    public CreateUserLevelUseCase(IUserLevelGateway gateway, IUserLevelMapper mapper, UserLevelValidator validator) {
        super(gateway, mapper);
        this.validator = validator;
    }

    public UserLevelOutputDTO execute(UserLevelCreateInputDTO dto) {

        if (validator.titleExists(dto.title()))
            throw new DuplicateUserLevelTitleException(dto.title());

        UserLevel userLevel = UserLevel.create(dto.title());

        userLevel = this.gateway.create(userLevel);
        return this.mapper.fromDomainToOutputDTO(userLevel);
    }
}
