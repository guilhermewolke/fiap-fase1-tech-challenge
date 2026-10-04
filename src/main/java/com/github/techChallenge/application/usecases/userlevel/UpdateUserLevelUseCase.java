package com.github.techChallenge.application.usecases.userlevel;

import com.github.techChallenge.application.exceptions.DuplicateUserLevelTitleException;
import com.github.techChallenge.application.exceptions.UserLevelNotFoundException;
import com.github.techChallenge.application.gateways.userlevel.IUserLevelGateway;
import com.github.techChallenge.application.validators.UserLevelValidator;
import com.github.techChallenge.domain.userlevel.IUserLevelMapper;
import com.github.techChallenge.domain.userlevel.UserLevel;
import com.github.techChallenge.domain.userlevel.dto.UserLevelOutputDTO;
import com.github.techChallenge.domain.userlevel.dto.UserLevelUpdateInputDTO;

public class UpdateUserLevelUseCase extends UserLevelUseCase {
    private final UserLevelValidator userLevelValidator;

    public UpdateUserLevelUseCase(IUserLevelGateway gateway, IUserLevelMapper mapper, UserLevelValidator userLevelValidator) {
        super(gateway, mapper);
        this.userLevelValidator = userLevelValidator;
    }

    public UserLevelOutputDTO execute(UserLevelUpdateInputDTO dto, int id) {
        if (!this.gateway.existById(id)) throw new UserLevelNotFoundException("Nível de usuário não encontrado");

        if (this.userLevelValidator.titleExists(dto.title(), id))
            throw new DuplicateUserLevelTitleException(dto.title());

        UserLevel userLevel = new UserLevel(id, dto.title());

        userLevel = this.gateway.update(userLevel, id);
        return this.mapper.fromDomainToOutputDTO(userLevel);
    }
}
